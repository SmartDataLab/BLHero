package com.xiugou.x1.game.server.module.shop.service.shop;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

/**
 * @author yh
 * @date 2023/8/23
 * @apiNote 仙境守卫积分兑换商店
 */
@Service
public class VillagePointsShopService extends AbstractShopService {
	@Override
	public ShopEnum shopEnum() {
		return ShopEnum.VILLAGE_POINTS_SHOP;
	}
}
