package com.xiugou.x1.game.server.module.shop.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.shop.model.ShopPlayer;


/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
@Service
public class ShopPlayerService extends OneToManyService<ShopPlayer> {
	
	protected ShopPlayer getEntity(long playerId, int shopId) {
		ShopPlayer entity = repository().getByKeys(playerId, shopId);
		if (entity == null) {
			entity = new ShopPlayer();
			entity.setPid(playerId);
			entity.setShopId(shopId);
			this.insert(entity);
		}
		return entity;
	}

}
