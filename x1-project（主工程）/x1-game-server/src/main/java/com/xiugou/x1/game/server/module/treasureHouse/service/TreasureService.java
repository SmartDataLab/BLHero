package com.xiugou.x1.game.server.module.treasureHouse.service;

import java.util.List;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.TreasureHouseCache;
import com.xiugou.x1.design.module.autogen.TreasureHouseAbstractCache.TreasureHouseCfg;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.shop.service.ShopSystemService;
import com.xiugou.x1.game.server.module.treasureHouse.model.Treasure;

/**
 * @author yh
 * @date 2023/8/23
 * @apiNote
 */
@Service
public class TreasureService extends OneToManyService<Treasure> {
	@Autowired
	private ShopSystemService shopSystemService;
	@Autowired
	private TreasureHouseCache treasureHouseCache;

	public Treasure getEntity(long playerId, int shopId) {
		Treasure entity = repository().getByKeys(playerId, shopId);
		if (entity == null) {
			entity = new Treasure();
			entity.setPeriod(1);
			entity.setPid(playerId);
			entity.setShopId(shopId);
			this.insert(entity);
		}
		if (entity.getNextResetTime().isBefore(LocalDateTimeUtil.now())) {
			int period = entity.getPeriod() + 1;
			List<TreasureHouseCfg> treasureHouseCfgs = treasureHouseCache.findInTypePeriodCollector(shopId, period);
			entity.setPeriod(period);
			if (treasureHouseCfgs == null) {
				entity.setPeriod(1);
			}
			entity.getProductDetail().clear();
			entity.setNextResetTime(shopSystemService.calculateResetTime(shopId));
			this.update(entity);
		}
		return entity;
	}

}
