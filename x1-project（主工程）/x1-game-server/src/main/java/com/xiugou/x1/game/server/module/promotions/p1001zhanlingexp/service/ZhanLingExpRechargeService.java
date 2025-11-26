/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingExpCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingExpAbstractCache.ZhanLingExpCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model.ZhanLingExp;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.promotion.P1001ZhanLingExp.ZhanLingExpRechargeMessage;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingExpRechargeService extends RechargeOrderingService {

	@Autowired
	private ZhanLingExpService zhanLingExpService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ZhanLingExpCache zhanLingExpCache;
	
	@Override
	public ProductType productType() {
		return ProductType.ZHAN_LING_EXP;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		int activityId = Integer.parseInt(rechargeProductCfg.getProductParam());
		promotionControlService.assertRunning(applicationSettings.getGameServerId(), activityId);
		ZhanLingExp entity = zhanLingExpService.getEntity(playerId, activityId);
		Asserts.isTrue(!entity.isBuyPremium(), TipsCode.ZL_BUY_HIGH);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(recharge.getProductId());
		int activityId = Integer.parseInt(rechargeProductCfg.getProductParam());
		
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(activityId);
		int upLevel = activeTemplateCfg.getOpenParams().get(1);
		
		ZhanLingExp entity = zhanLingExpService.getEntity(playerId, activityId);
		int addLevel = 0;
		for(int i = 1; i <= upLevel; i++) {
			int level = entity.getLevel() + i;
			ZhanLingExpCfg cfg = zhanLingExpCache.findInActivityIdLevelIndex(activityId, level);
			if(cfg == null) {
				break;
			}
			addLevel = i;
		}
		entity.setLevel(entity.getLevel() + addLevel);
		entity.setBuyPremium(true);
		List<RewardThing> rewardList = zhanLingExpService.takeRewardsAndMark(entity);
		outRewards.addAll(rewardList);
		zhanLingExpService.update(entity);
		
		ZhanLingExpRechargeMessage.Builder builder = ZhanLingExpRechargeMessage.newBuilder();
		builder.setTypeId(entity.getTypeId());
		builder.setLevel(entity.getLevel());
		builder.setFreeRewardLv(entity.getFreeRewardLv());
		builder.setPremiumRewardLv(entity.getPremiumRewardLv());
		playerContextManager.push(playerId, ZhanLingExpRechargeMessage.Proto.ID, builder.build());
	}
}
