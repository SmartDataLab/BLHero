/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingExpCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingExpAbstractCache.ZhanLingExpCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model.ZhanLingExp;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service.ZhanLingExpService;

import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.PbZhanLingExpCfg;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpBuyLevelRequest;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpBuyLevelResponse;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpCfgRequest;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpCfgResponse;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpInfoRequest;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpInfoResponse;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpRewardResponse;

/**
 * @author hyy
 *
 */
@Controller
public class ZhanLingExpHandler {

	@Autowired
	private ZhanLingExpService zhanLingExpService;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ZhanLingExpCache zhanLingExpCache;
	@Autowired
	private ThingService thingService;
	
	@PlayerCmd
	public ZhanLingExpInfoResponse info(PlayerContext playerContext, ZhanLingExpInfoRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_EXP.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingExp entity = zhanLingExpService.getEntity(playerContext.getId(), request.getTypeId());
		
		ZhanLingExpInfoResponse.Builder response = ZhanLingExpInfoResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		response.setLevel(entity.getLevel());
		response.setExp(entity.getExp());
		response.setFreeRewardLv(entity.getFreeRewardLv());
		response.setPremiumRewardLv(entity.getPremiumRewardLv());
		response.setBuyPremium(entity.isBuyPremium());
		return response.build();
	}
	
	@PlayerCmd
	public ZhanLingExpRewardResponse reward(PlayerContext playerContext, ZhanLingExpRewardRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_EXP.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingExp entity = zhanLingExpService.getEntity(playerContext.getId(), request.getTypeId());
		
		List<RewardThing> rewardList = zhanLingExpService.takeRewardsAndMark(entity);
		Asserts.isTrue(!rewardList.isEmpty(), TipsCode.ZL_NO_REWARD);
		
		zhanLingExpService.update(entity);
		
		thingService.add(playerContext.getId(), rewardList, GameCause.ZLEXP_REWARD);
		
		ZhanLingExpRewardResponse.Builder response = ZhanLingExpRewardResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		response.setFreeRewardLv(entity.getFreeRewardLv());
		response.setPremiumRewardLv(entity.getPremiumRewardLv());
		return response.build();
	}
	
	@PlayerCmd
	public ZhanLingExpBuyLevelResponse buyLevel(PlayerContext playerContext, ZhanLingExpBuyLevelRequest request) {
		Asserts.isTrue(request.getBuyLevel() > 0, TipsCode.ERROR_PARAM);
		
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_EXP.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingExp entity = zhanLingExpService.getEntity(playerContext.getId(), request.getTypeId());
		
		List<CostThing> costList = new ArrayList<>();
		int addLevel = 0;
		for(int i = 1; i <= request.getBuyLevel(); i++) {
			int level = entity.getLevel() + i;
			ZhanLingExpCfg cfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), level);
			if(cfg == null) {
				break;
			}
			costList.add(cfg.getBuyLevelCost());
			addLevel = i;
		}
		
		Asserts.isTrue(addLevel > 0, TipsCode.ZL_MAX_LEVEL);
		Asserts.isTrue(!costList.isEmpty(), TipsCode.ZL_MAX_LEVEL);
		thingService.cost(playerContext.getId(), costList, GameCause.ZLEXP_BUY_LEVEL);
		
		entity.setLevel(entity.getLevel() + addLevel);
		List<RewardThing> rewardList = zhanLingExpService.takeRewardsAndMark(entity);
		zhanLingExpService.update(entity);
		
		thingService.add(playerContext.getId(), rewardList, GameCause.ZLEXP_BUY_LEVEL);
		
		ZhanLingExpBuyLevelResponse.Builder response = ZhanLingExpBuyLevelResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		response.setLevel(entity.getLevel());
		response.setFreeRewardLv(entity.getFreeRewardLv());
		response.setPremiumRewardLv(entity.getPremiumRewardLv());
		return response.build();
	}
	
	@PlayerCmd
	public ZhanLingExpCfgResponse cfg(PlayerContext playerContext, ZhanLingExpCfgRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_EXP.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingExpCfgResponse.Builder response = ZhanLingExpCfgResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		for(ZhanLingExpCfg cfg : zhanLingExpCache.getInActivityIdCollector(request.getTypeId())) {
			response.addCfgs(build(cfg));
		}
		return response.build();
	}
	
	public PbZhanLingExpCfg build(ZhanLingExpCfg cfg) {
		PbZhanLingExpCfg.Builder builder = PbZhanLingExpCfg.newBuilder();
		builder.setLevel(cfg.getLevel());
		builder.setNeedExp(cfg.getNeedExp());
		builder.addAllFreeReward(PbHelper.buildReward(cfg.getFreeReward()));
		builder.addAllPremiumReward(PbHelper.buildReward(cfg.getPremiumReward()));
		builder.setBuyLevelCost(PbHelper.build(cfg.getBuyLevelCost()));
		return builder.build();
	}
}
