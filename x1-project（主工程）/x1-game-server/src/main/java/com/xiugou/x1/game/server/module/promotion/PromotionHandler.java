/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion;

import java.time.LocalDateTime;

import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.message.PromotionEndMessage;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;

import pb.xiugou.x1.protobuf.promotion.Promotion.PbPromotionControl;
import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class PromotionHandler extends AbstractModuleHandler {

	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private ApplicationSettings applicationSettings;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		PromotionInfoResponse.Builder response = PromotionInfoResponse.newBuilder();
		Player player = playerService.getEntity(playerId);
		
		LocalDateTime now = LocalDateTimeUtil.now();
		for(PromotionControl control : promotionControlService.allControlsInServer(player.getServerId())) {
			if(now.isBefore(control.getStartTime()) || now.isAfter(control.getEndTime())) {
				continue;
			}
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrNull(control.getTypeId());
			if(activeTemplateCfg == null || player.getLevel() < activeTemplateCfg.getMinLevel()) {
				continue;
			}
			if(applicationSettings.isGameArraignType() && "IOS".equals(playerContext.getDeviceType())) {
				if(activeTemplateCfg.getHide() == 1) {
					continue;
				}
				response.addPromotionControls(buildOnLogin(playerId, control));
			} else {
				response.addPromotionControls(buildOnLogin(playerId, control));
			}
		}
		playerContextManager.push(playerId, PromotionInfoResponse.Proto.ID, response.build());
	}
	
	@InternalCmd
	public void handlePromotionEnd(PromotionEndMessage message) {
		PromotionLogicType logicType = PromotionLogicType.valueOf(message.getControl().getLogicType());
		PromotionLogicService logicService = PromotionControlService.getLogicService(logicType);
		logicService.handlePromotionEnd(message.getPlayerId(), message.getControl());
	}
	
	
	private PbPromotionControl buildOnLogin(long playerId, PromotionControl promotionControl) {
		PbPromotionControl.Builder builder = PbPromotionControl.newBuilder();
		builder.setStage(promotionControl.getStage().getValue());
		builder.setTypeId(promotionControl.getTypeId());
		builder.setStartTime(LocalDateTimeUtil.toEpochMilli(promotionControl.getStartTime()));
		builder.setStillTime(LocalDateTimeUtil.toEpochMilli(promotionControl.getStillTime()));
		builder.setEndTime(LocalDateTimeUtil.toEpochMilli(promotionControl.getEndTime()));
		PromotionLogicService logicService = PromotionControlService.getLogicService(PromotionLogicType.valueOf(promotionControl.getLogicType()));
		if(logicService == null) {
			builder.setRedPoint(false);
		} else {
			builder.setRedPoint(logicService.showLoginRedPoint(playerId, promotionControl.getTypeId()));
		}
		return builder.build();
	}
}
