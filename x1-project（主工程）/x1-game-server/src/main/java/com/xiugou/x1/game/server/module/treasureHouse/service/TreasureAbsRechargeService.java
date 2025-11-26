package com.xiugou.x1.game.server.module.treasureHouse.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiugou.x1.design.module.autogen.TreasureHouseAbstractCache.TreasureHouseCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.treasureHouse.struct.TreasureData;

/**
 * @author yh
 * @date 2023/10/18
 * @apiNote
 */
public abstract class TreasureAbsRechargeService {

	public static Map<ShopEnum, TreasureAbsRechargeService> treasureRechargeService = new HashMap<>();

	public static void register(TreasureAbsRechargeService service) {
		treasureRechargeService.put(service.getTreasureType(), service);
	}

	public static TreasureAbsRechargeService getService(ShopEnum shopEnum) {
		return treasureRechargeService.get(shopEnum);
	}

	public TreasureAbsRechargeService() {
		register(this);
	}

	protected abstract ShopEnum getTreasureType();

	protected abstract void checkOrdering(long playerId, TreasureData treasureData, TreasureHouseCfg treasureHouseCfg);
	
	protected abstract void buySuccess(Recharge recharge, List<RewardThing> outRewards, TreasureData treasureData,
			TreasureHouseCfg treasureHouseCfg);
}
