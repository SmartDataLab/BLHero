package com.xiugou.x1.game.server.module.shop.service.shop;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

/**
 * @author yh
 * @date 2023/7/27
 * @apiNote 炼狱积分兑换商店
 */
@Service
public class PurgatoryPointsShopService extends AbstractShopService{
	@Override
	public ShopEnum shopEnum() {
		return ShopEnum.PURGATORY_POINTS_SHOP;
	}
}
