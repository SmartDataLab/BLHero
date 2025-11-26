package com.xiugou.x1.game.server.module.shop.service.shop;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

/**
 * @author yh
 * @date 2023/8/24
 * @apiNote 装备材料兑换商店
 */
@Service
public class EquipMaterialsService extends AbstractShopService{
    @Override
    public ShopEnum shopEnum() {
        return ShopEnum.EQUIP_MATERIALS_SHOP;
    }
}
