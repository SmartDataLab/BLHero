/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.service;

import java.util.List;

import org.gaming.prefab.task.TaskStatus;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.MeiRiChongZhiCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.MeiRiChongZhiAbstractCache.MeiRiChongZhiCfg;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.model.MeiRiChongZhi;
import com.xiugou.x1.game.server.module.recharge.event.RechargeEvent;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbTask;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiChangeMessage;
import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author yy
 *
 */
@Service
public class MeiRiChongZhiService extends OneToManyService<MeiRiChongZhi> {

	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private TimeSetting timeSetting;
	@Autowired
	private MeiRiChongZhiCache meiRiChongZhiCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	public MeiRiChongZhi getEntity(long playerId, int typeId) {
		MeiRiChongZhi entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(typeId);
			entity = new MeiRiChongZhi();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			entity.setTypeName(activeTemplateCfg.getName());
			this.insert(entity);
		}
		boolean needUpdate = false;
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.setRechargeValue(0);
			entity.setCanRewardId(0);
			entity.getTakeRewards().clear();
			needUpdate = true;
		}
		if(LocalDateTimeUtil.now().isAfter(entity.getDailyTime())) {
			entity.setDailyTime(timeSetting.nextDayOTime());
			entity.setRechargeValue(0);
			entity.setRewardSettle(false);
			needUpdate = true;
		}
		if(needUpdate) {
			this.update(entity);
		}
		return entity;
	}
	
	@Subscribe
	private void listen(RechargeEvent event) {
		List<ActiveTemplateCfg> templates = activeTemplateCache.getInLogicTypeCollector(PromotionLogicType.MEI_RI_CHONG_ZHI.getValue());
		for(ActiveTemplateCfg cfg : templates) {
			PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), cfg.getId());
			if(!promotionControl.isRunning()) {
				continue;
			}
			MeiRiChongZhi entity = this.getEntity(event.getPid(), promotionControl.getTypeId());
			entity.setRechargeValue(entity.getRechargeValue() + event.getPayMoney());
			
			MeiRiChongZhiCfg meiRiChongZhiCfg = meiRiChongZhiCache.findInActivityIdRewardIdIndex(entity.getTypeId(), entity.getCanRewardId() + 1);
			if(!entity.isRewardSettle() && meiRiChongZhiCfg != null && entity.getRechargeValue() >= meiRiChongZhiCfg.getTargetNum()) {
				entity.setRewardSettle(true);
				entity.setCanRewardId(entity.getCanRewardId() + 1);
			}
			this.update(entity);
			
			//推送红点
			if(entity.getCanRewardId() > entity.getTakeRewards().size()) {
				PromotionRedPointMessage.Builder builder = PromotionRedPointMessage.newBuilder();
				builder.setTypeId(entity.getTypeId());
				builder.setRedPoint(true);
				playerContextManager.push(entity.getPid(), PromotionRedPointMessage.Proto.ID, builder.build());
			}
			
			//推送活动数据
			MeiRiChongZhiChangeMessage.Builder builder = MeiRiChongZhiChangeMessage.newBuilder();
			builder.setTypeId(entity.getTypeId());
			builder.setTasks(buildToTask(entity, meiRiChongZhiCfg));
			playerContextManager.push(entity.getPid(), MeiRiChongZhiChangeMessage.Proto.ID, builder.build());
		}
	}
	
	public PbTask buildToTask(MeiRiChongZhi entity, MeiRiChongZhiCfg cfg) {
		PbTask.Builder data = PbTask.newBuilder();
		data.setId(cfg.getRewardId());
		data.setProgress(entity.getCanRewardId());
		if(entity.getCanRewardId() >= cfg.getRewardId()) {
			data.setStatus(TaskStatus.DONE.getValue());
		}
		if(entity.getTakeRewards().contains(cfg.getRewardId())) {
			data.setStatus(TaskStatus.EMPTY.getValue());
		}
		return data.build();
	}
}
