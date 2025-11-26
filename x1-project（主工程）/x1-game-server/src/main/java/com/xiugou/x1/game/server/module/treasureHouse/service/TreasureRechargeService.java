package com.xiugou.x1.game.server.module.treasureHouse.service;

import java.util.List;
import java.util.Map;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.TreasureHouseCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.module.autogen.TreasureHouseAbstractCache.TreasureHouseCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.service.ShopSystemService;
import com.xiugou.x1.game.server.module.treasureHouse.model.Treasure;
import com.xiugou.x1.game.server.module.treasureHouse.struct.TreasureData;
import com.xiugou.x1.game.server.module.vip.model.Vip;
import com.xiugou.x1.game.server.module.vip.service.VipService;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbKeyV;
import pb.xiugou.x1.protobuf.treasure.Treasure.TreasureBuyMessage;

/**
 * @author yh
 * @date 2023/8/23
 * @apiNote 珍宝阁普通商品购买
 */
@Service
public class TreasureRechargeService extends RechargeOrderingService {

    @Autowired
    private TreasureService treasureService;
    @Autowired
    private TreasureHouseCache treasureHouseCache;
    @Autowired
    private RechargeProductCache rechargeProductCache;
    @Autowired
    private VipService vipService;
    @Autowired
    private ShopSystemService shopSystemService;

    @Override
    public ProductType productType() {
        return ProductType.TREASURE_RECHARGE;
    }

    /**
     * extraInfo 普通礼拜（包括特权礼包） 只需要赋shopId  定制礼包需要传选定道具ID
     */
    @Override
    public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
        TreasureData treasureData = GsonUtil.parseJson(productData, TreasureData.class);
        int shopId = treasureData.getShopId();
        
        Treasure treasure = treasureService.getEntity(playerId, shopId);
        TreasureHouseCfg treasureHouseCfg = treasureHouseCache.getInTypePeriodRechargeIdIndex(shopId, treasure.getPeriod(), rechargeProductCfg.getId());
        Asserts.isTrue(shopSystemService.limitCondition(playerId, treasureHouseCfg.getCondition()), TipsCode.TREASURE_BUY_CONDITION_SATISFY);

		if (treasureHouseCfg.getLimitType() == 1) {
			// 当期限购
			Asserts.isTrue(treasure.getProductDetail().getOrDefault(treasureHouseCfg.getId(), 0) < treasureHouseCfg
					.getLimitNum(), TipsCode.TREASURE_BUY_LIMIT);
		} else if (treasureHouseCfg.getLimitType() == 2) {
			// 终身限购
			Asserts.isTrue(treasure.getLimitProductDetail().getOrDefault(treasureHouseCfg.getId(), 0) < treasureHouseCfg
					.getLimitNum(), TipsCode.TREASURE_BUY_LIMIT);
		}
		
		TreasureAbsRechargeService service = TreasureAbsRechargeService.getService(ShopEnum.valueOf(shopId));
		if(service != null) {
			service.checkOrdering(playerId, treasureData, treasureHouseCfg);
		}
    }

    @Override
    public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
        TreasureData treasureData = GsonUtil.parseJson(recharge.getProductData(), TreasureData.class);

        int shopId = treasureData.getShopId();
        Treasure treasure = treasureService.getEntity(playerId, shopId);
        TreasureHouseCfg treasureHouseCfg = treasureHouseCache.getInTypePeriodRechargeIdIndex(shopId, treasure.getPeriod(), recharge.getProductId());

        int buyNum = 0;
        if (treasureHouseCfg.getLimitType() == 1) {
            //当期限购
            Map<Integer, Integer> productDetail = treasure.getProductDetail();
            productDetail.put(recharge.getProductId(), productDetail.getOrDefault(recharge.getProductId(), 0) + 1);
            buyNum = productDetail.get(recharge.getProductId());
        } else if (treasureHouseCfg.getLimitType() == 2) {
            //终身限购
            Map<Integer, Integer> productDetail = treasure.getLimitProductDetail();
            productDetail.put(recharge.getProductId(), productDetail.getOrDefault(recharge.getProductId(), 0) + 1);
            buyNum = productDetail.get(recharge.getProductId());
        }
        treasureService.update(treasure);
        
        TreasureAbsRechargeService service = TreasureAbsRechargeService.getService(ShopEnum.valueOf(shopId));
        if(service != null) {
        	service.buySuccess(recharge, outRewards, treasureData, treasureHouseCfg);
        }

        TreasureBuyMessage.Builder builder = TreasureBuyMessage.newBuilder();
        builder.setShopId(shopId);
        PbKeyV.Builder pbkeyV = PbKeyV.newBuilder();
        pbkeyV.setKey(recharge.getProductId());
        pbkeyV.setValue(buyNum);
        builder.setProduct(pbkeyV.build());
        playerContextManager.push(playerId, TreasureBuyMessage.Proto.ID, builder.build());
        
        //增加特权
        RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(recharge.getProductId());
        if ("".equals(rechargeProductCfg.getProductParam())) {
            return;
        }
        Vip vip = vipService.getEntity(playerId);
        List<Integer> privilegeList = vip.getPrivilegeList();
        if (privilegeList.contains(Integer.parseInt(rechargeProductCfg.getProductParam()))) {
            return;
        }
        privilegeList.add(Integer.parseInt(rechargeProductCfg.getProductParam()));
        vipService.update(vip);
    }
}












