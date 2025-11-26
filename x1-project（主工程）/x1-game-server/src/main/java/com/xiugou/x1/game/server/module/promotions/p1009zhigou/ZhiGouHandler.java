/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1009zhigou;

import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhiGouLiBaoCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ZhiGouLiBaoAbstractCache.ZhiGouLiBaoCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.model.ZhiGou;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.service.ZhiGouPromotionService;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.service.ZhiGouService;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouLimit;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbKeyV;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.PbZhiGouCfg;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouCfgRequest;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouCfgResponse;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouInfoRequest;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouInfoResponse;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouTakeFreeRequest;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouTakeFreeResponse;
import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author yy
 *
 */
@Controller
public class ZhiGouHandler {

	@Autowired
	private ZhiGouService zhiGouService;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ZhiGouLiBaoCache zhiGouLiBaoCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private ZhiGouPromotionService zhiGouPromotionService;
	
	@PlayerCmd
	public ZhiGouInfoResponse info(PlayerContext playerContext, ZhiGouInfoRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHI_GOU.getValue(), TipsCode.ERROR_PARAM);
		
		ZhiGou entity = zhiGouService.getEntity(playerContext.getId(), request.getTypeId());
		
		ZhiGouInfoResponse.Builder response = ZhiGouInfoResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		for(ZhiGouLimit limit : entity.getLimitBuys().values()) {
			PbKeyV.Builder data = PbKeyV.newBuilder();
			data.setKey(limit.getId());
			data.setValue(limit.getNum());
			response.addKeyvs(data.build());
		}
		return response.build();
	}
	
	@PlayerCmd
	public ZhiGouCfgResponse cfgs(PlayerContext playerContext, ZhiGouCfgRequest request) {
		List<ZhiGouLiBaoCfg> cfgs = zhiGouLiBaoCache.findInActivityIdCollector(request.getTypeId());
		if(cfgs == null) {
			return ZhiGouCfgResponse.getDefaultInstance();
		}
		
		ZhiGouCfgResponse.Builder response = ZhiGouCfgResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		for(ZhiGouLiBaoCfg cfg : cfgs) {
			PbZhiGouCfg.Builder data = PbZhiGouCfg.newBuilder();
			data.setId(cfg.getId());
			data.setChargeProductId(cfg.getChargeProductId());
			data.addAllDiffReward(PbHelper.buildReward(cfg.getDiffReward()));
			data.setLimitType(cfg.getLimitType());
			data.setLimitNum(cfg.getLimitNum());
			response.addCfgs(data.build());
		}
		return response.build();
	}
	
	@PlayerCmd
	public ZhiGouTakeFreeResponse takeFree(PlayerContext playerContext, ZhiGouTakeFreeRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHI_GOU.getValue(), TipsCode.ERROR_PARAM);
		
		ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(request.getId());
		Asserts.isTrue(zhiGouLiBaoCfg.getActivityId() == request.getTypeId(), TipsCode.ZG_TYPE_ERROR);
		
		ZhiGou entity = zhiGouService.getEntity(playerContext.getId(), request.getTypeId());
		ZhiGouLimit zhiGouLimit = entity.getLimitBuys().get(zhiGouLiBaoCfg.getId());
		if(zhiGouLimit != null) {
			Asserts.isTrue(zhiGouLimit.getNum() < zhiGouLiBaoCfg.getLimitNum(), TipsCode.ZG_BUY_LIMIT);
		}
		
		if(zhiGouLimit == null) {
			zhiGouLimit = new ZhiGouLimit();
			zhiGouLimit.setId(zhiGouLiBaoCfg.getId());
			entity.getLimitBuys().put(zhiGouLimit.getId(), zhiGouLimit);
		}
		zhiGouLimit.setNum(zhiGouLimit.getNum() + 1);
		zhiGouService.update(entity);
		
		thingService.add(playerContext.getId(), zhiGouLiBaoCfg.getDiffReward(), GameCause.ZG_FREE_REWARD);
		
		PromotionRedPointMessage.Builder redMessage = PromotionRedPointMessage.newBuilder();
		redMessage.setTypeId(request.getTypeId());
		redMessage.setRedPoint(zhiGouPromotionService.showLoginRedPoint(playerContext.getId(), request.getTypeId()));
		playerContextManager.push(playerContext.getId(), PromotionRedPointMessage.Proto.ID, redMessage.build());
		
		
		ZhiGouTakeFreeResponse.Builder response = ZhiGouTakeFreeResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		PbKeyV.Builder data = PbKeyV.newBuilder();
		data.setKey(zhiGouLimit.getId());
		data.setValue(zhiGouLimit.getNum());
		response.setKeyv(data.build());
		return response.build();
	}
}
