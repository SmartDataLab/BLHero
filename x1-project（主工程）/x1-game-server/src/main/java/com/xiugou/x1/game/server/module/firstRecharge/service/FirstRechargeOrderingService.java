/**
 * 
 */
package com.xiugou.x1.game.server.module.firstRecharge.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.FirstRechargeCache;
import com.xiugou.x1.design.module.autogen.FirstRechargeAbstractCache.FirstRechargeCfg;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.firstRecharge.model.FirstRecharge;
import com.xiugou.x1.game.server.module.firstRecharge.struct.FirstRechargeData;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.firstrecharge.FirstRecharge.FirstRechargePushMessage;

/**
 * @author YY
 *
 */
@Service
public class FirstRechargeOrderingService extends RechargeOrderingService {

	@Autowired
	private FirstRechargeService firstRechargeService;
	@Autowired
	private FirstRechargeCache firstRechargeCache;
	
	@Override
	public ProductType productType() {
		return ProductType.FIRST_RECHARGE;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		FirstRecharge firstRecharge = firstRechargeService.getEntity(playerId);
		
		FirstRechargeData firstRechargeData = firstRecharge.getRechargeDatas().get(rechargeProductCfg.getId());
		Asserts.isTrue(firstRechargeData == null, TipsCode.FIRST_RECHARGE_HAS_BUY);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		FirstRecharge firstRecharge = firstRechargeService.getEntity(playerId);
		
		FirstRechargeData firstRechargeData = new FirstRechargeData();
		firstRechargeData.setRechargeId(recharge.getProductId());
		firstRechargeData.setRewardDay(1);
		firstRechargeData.setCanTakeDay(1);
		firstRecharge.getRechargeDatas().put(firstRechargeData.getRechargeId(), firstRechargeData);
        firstRechargeService.update(firstRecharge);
        
        FirstRechargeCfg firstRechargeCfg = firstRechargeCache.getInRechargeIdDayIndex(firstRechargeData.getRechargeId(), 1);
        outRewards.addAll(firstRechargeCfg.getReward());
        
        FirstRechargePushMessage.Builder builder = FirstRechargePushMessage.newBuilder();
        builder.setRechargeData(firstRechargeService.build(firstRechargeData));
        playerContextManager.push(playerId, FirstRechargePushMessage.Proto.ID, builder.build());
	}

}
