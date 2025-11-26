/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.service;

import org.gaming.ruler.eventbus.EventBus;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.recharge.event.RechargeEvent;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;

/**
 * @author YY
 *
 */
@Service
public class RechargePlayerService extends PlayerOneToOneResetableService<RechargePlayer> {

	@Override
	protected RechargePlayer createWhenNull(long entityId) {
		RechargePlayer entity = new RechargePlayer();
		entity.setPid(entityId);
		return entity;
	}
	
	/**
	 * 记录充值信息
	 * @param playerId
	 * @param recharge
	 */
	public void recordRecharge(long playerId, Recharge recharge) {
		RechargePlayer entity = this.getEntity(playerId);
		
		entity.setTotalPay(entity.getTotalPay() + recharge.getPayMoney());
		entity.setDailyPay(entity.getDailyPay() + recharge.getPayMoney());
		if(!recharge.isVirtual()) {
			entity.setRealTotalPay(entity.getRealTotalPay() + recharge.getPayMoney());
			entity.setRealDailyPay(entity.getRealDailyPay() + recharge.getPayMoney());
		}
		entity.getBuyProducts().add(recharge.getProductId());
		this.update(entity);
		
		EventBus.post(RechargeEvent.of(playerId, recharge));
	}

	@Override
	protected void doDailyReset(RechargePlayer entity) {
		entity.setDailyPay(0);
		entity.setRealDailyPay(0);
	}
}
