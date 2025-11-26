/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model.ZhanLingGoal;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalRechargeMessage;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingGoalRechargeService extends RechargeOrderingService {

	@Autowired
	private ZhanLingGoalService zhanLingGoalService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public ProductType productType() {
		return ProductType.ZHAN_LING_GOAL;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		int activityId = Integer.parseInt(rechargeProductCfg.getProductParam());
		promotionControlService.assertRunning(applicationSettings.getGameServerId(), activityId);
		ZhanLingGoal entity = zhanLingGoalService.getEntity(playerId, activityId);
		Asserts.isTrue(!entity.isBuyPremium(), TipsCode.ZL_BUY_HIGH);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(recharge.getProductId());
		int activityId = Integer.parseInt(rechargeProductCfg.getProductParam());
		ZhanLingGoal entity = zhanLingGoalService.getEntity(playerId, activityId);
		entity.setBuyPremium(true);
		zhanLingGoalService.update(entity);
		
		ZhanLingGoalRechargeMessage.Builder builder = ZhanLingGoalRechargeMessage.newBuilder();
		builder.setTypeId(entity.getTypeId());
		playerContextManager.push(playerId, ZhanLingGoalRechargeMessage.Proto.ID, builder.build());
	}
}
