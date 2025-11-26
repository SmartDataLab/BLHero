/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

/**
 * @author YY
 *
 */
public abstract class RechargeOrderingService {

	@Autowired
	protected RechargeProductCache rechargeProductCache;
	@Autowired
	protected PlayerContextManager playerContextManager;
	
	public RechargeOrderingService() {
		RechargeService.register(this);
	}
	/**
	 * 商品类型
	 * @return
	 */
	public abstract ProductType productType();
	/**
	 * 检查是否可以生成订单
	 * @param playerId
	 * @param rechargeProductCfg
	 * @param extraInfo
	 */
	public abstract void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData);
	/**
	 * 充值成功时被调用
	 * @param playerId
	 * @param recharge
	 * @param outRewards 把具体充值商品设定的奖励加入到此列表中
	 */
	public abstract void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards);
	/**
	 * 在任意充值成功后被调用
	 * @param playerId
	 * @param recharge
	 */
	public void afterAnyBuySuccess(long playerId, Recharge recharge) {
		//to be override
	}
}
