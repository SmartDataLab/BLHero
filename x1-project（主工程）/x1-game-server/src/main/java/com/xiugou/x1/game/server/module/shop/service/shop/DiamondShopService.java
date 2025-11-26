package com.xiugou.x1.game.server.module.shop.service.shop;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

import org.springframework.stereotype.Service;

/**
 * @author yh
 * @date 2023/10/23
 * @apiNote 仙玉商店
 */
@Service
public class DiamondShopService extends AbstractShopService{
    @Override
    public ShopEnum shopEnum() {
        return ShopEnum.DIAMOND_SHOP;
    }
}
