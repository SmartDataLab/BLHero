package com.xiugou.x1.game.server.module.treasureHouse.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.autogen.TreasureHouseAbstractCache.TreasureHouseCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;
import com.xiugou.x1.game.server.module.recharge.service.RechargePlayerService;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.treasureHouse.struct.TreasureData;

/**
 * @author yh
 * @date 2023/10/18
 * @apiNote
 */
@Service
public class TreasureDiamondService extends TreasureAbsRechargeService {
	@Autowired
	private RechargePlayerService rechargePlayerService;

	@Override
	protected ShopEnum getTreasureType() {
		return ShopEnum.ZBG_DIAMOND_SHOP;
	}

	@Override
	protected void buySuccess(Recharge recharge, List<RewardThing> outRewards, TreasureData treasureData,
			TreasureHouseCfg treasureHouseCfg) {
		RechargePlayer rechargePlayer = rechargePlayerService.getEntity(recharge.getPlayerId());
		Set<Integer> buyProducts = rechargePlayer.getBuyProducts();
		if (buyProducts.contains(recharge.getProductId())) {
			outRewards.addAll(treasureHouseCfg.getReward());
		}
	}

	@Override
	protected void checkOrdering(long playerId, TreasureData treasureData, TreasureHouseCfg treasureHouseCfg) {
		//没有需要验证的
	}
}
