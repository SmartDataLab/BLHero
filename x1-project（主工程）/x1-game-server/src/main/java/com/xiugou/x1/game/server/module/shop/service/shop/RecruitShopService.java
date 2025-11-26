package com.xiugou.x1.game.server.module.shop.service.shop;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

/**
 * @author yh
 * @date 2023/8/29
 * @apiNote 招募积分商店
 */
@Service
public class RecruitShopService extends AbstractShopService{
    @Override
    public ShopEnum shopEnum() {
        return ShopEnum.RECRUIT_POINT;
    }
}
