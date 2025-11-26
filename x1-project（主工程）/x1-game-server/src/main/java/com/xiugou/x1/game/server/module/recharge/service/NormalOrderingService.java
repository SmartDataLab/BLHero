/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

/**
 * @author YY
 *
 */
@Service
public class NormalOrderingService extends RechargeOrderingService {

	@Override
	public ProductType productType() {
		return ProductType.NORMAL;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		//普通充值不需要做特殊的检查
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		//购买成功的奖励已经在充值商品表中发放
	}
}
