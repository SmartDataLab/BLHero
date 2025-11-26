package com.xiugou.x1.game.server.module.shop.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.model.ShopPlayer;
import com.xiugou.x1.game.server.module.shop.model.ShopSystem;
import com.xiugou.x1.game.server.module.shop.struct.ProductDetailData;

import pb.xiugou.x1.protobuf.exchangeshop.ExchangeShop.PbProduct;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
public abstract class AbstractShopService {

    @Autowired
    private ShopPlayerService shopPlayerService;
    @Autowired
    private ShopSystemService shopSystemService;
    public static Map<ShopEnum, AbstractShopService> shopService = new HashMap<>();

    public static void register(AbstractShopService service) {
        shopService.put(service.shopEnum(), service);
    }

    public static AbstractShopService getService(ShopEnum shopEnum) {
        return shopService.get(shopEnum);
    }

    public AbstractShopService() {
        register(this);
    }

    public abstract ShopEnum shopEnum();

    public ShopPlayer getShopPlayer(long pid) {
        ShopPlayer shopPlayer = shopPlayerService.getEntity(pid, shopEnum().getShopId());

        ShopSystem shopSystem = getShopSystem();
        if (shopSystem.getIncreaseRound() != shopPlayer.getIncreaseRound()) {
            shopPlayer.getProductDetail().clear();
            shopPlayer.setIncreaseRound(shopSystem.getIncreaseRound());
            shopPlayerService.update(shopPlayer);
        }
        return shopPlayer;
    }
    
    public ShopSystem getShopSystem() {
    	return shopSystemService.getEntity(shopEnum().getShopId());
    }

    public static PbProduct build(ProductDetailData productDetailData) {
        PbProduct.Builder pbProduct = PbProduct.newBuilder();
        pbProduct.setProductId(productDetailData.getProductId());
        pbProduct.setBoughtNum(productDetailData.getBuyNum());
        pbProduct.setFreeNum(productDetailData.getFreeNum());
        pbProduct.setAdvNum(productDetailData.getAdvNum());
        return pbProduct.build();
    }
}

