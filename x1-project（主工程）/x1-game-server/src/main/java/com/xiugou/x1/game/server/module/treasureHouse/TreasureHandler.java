package com.xiugou.x1.game.server.module.treasureHouse;

import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.TreasureHouseCache;
import com.xiugou.x1.design.module.autogen.TreasureHouseAbstractCache.TreasureHouseCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.treasureHouse.model.Treasure;
import com.xiugou.x1.game.server.module.treasureHouse.service.TreasureService;

import pb.xiugou.x1.protobuf.treasure.Treasure.PbRechargeShop;
import pb.xiugou.x1.protobuf.treasure.Treasure.TreasureInfoResponse;
import pb.xiugou.x1.protobuf.treasure.Treasure.TreasureTakeRewardRequest;
import pb.xiugou.x1.protobuf.treasure.Treasure.TreasureTakeRewardResponse;

/**
 * @author yh
 * @date 2023/8/24
 * @apiNote 珍宝阁
 */
@Controller
public class TreasureHandler extends AbstractModuleHandler {
	
    @Autowired
    private TreasureService treasureService;
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private TreasureHouseCache treasureHouseCache;
    @Autowired
    private ThingService thingService;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		ShopEnum[] shopIds = new ShopEnum[] { ShopEnum.ZBG_DIAMOND_SHOP, ShopEnum.ZBG_TEHUI_SHOP, ShopEnum.ZBG_DAILY_SHOP, ShopEnum.ZBG_WEEKLY_SHOP };
        TreasureInfoResponse.Builder response = TreasureInfoResponse.newBuilder();
        for (ShopEnum shopId : shopIds) {
        	Treasure treasure = treasureService.getEntity(playerId, shopId.getShopId());
            response.addRechargeShops(build(treasure));
        }
        response.setServerOpenDay(serverInfoService.getOpenedDay());
        playerContextManager.push(playerId, TreasureInfoResponse.Proto.ID, response.build());
    }

    public PbRechargeShop build(Treasure treasure) {
    	PbRechargeShop.Builder builder = PbRechargeShop.newBuilder();
    	builder.setShopId(treasure.getShopId());
        builder.setPeriod(treasure.getPeriod());
        //本轮商品详情
        for (Map.Entry<Integer, Integer> entry : treasure.getProductDetail().entrySet()) {
            builder.addProduct(PbHelper.build(entry));
        }
        //终身限购商品
        for (Map.Entry<Integer, Integer> entry : treasure.getLimitProductDetail().entrySet()) {
            builder.addProduct(PbHelper.build(entry));
        }
        builder.setResetTime(LocalDateTimeUtil.toEpochMilli(treasure.getNextResetTime()));
        return builder.build();
    }
    
    @PlayerCmd
    public TreasureTakeRewardResponse takeReward(PlayerContext playerContext, TreasureTakeRewardRequest request) {
    	Treasure treasure = treasureService.getEntity(playerContext.getId(), request.getShopId());
    	TreasureHouseCfg treasureHouseCfg = treasureHouseCache.getInTypePeriodRewardIdIndex(treasure.getShopId(), treasure.getPeriod(), request.getRewardId());
    	
    	int takeNum = 0;
    	if(treasureHouseCfg.getLimitType() == 1) {
    		takeNum = treasure.getProductDetail().getOrDefault(request.getRewardId(), 0);
    	} else if(treasureHouseCfg.getLimitNum() == 2) {
    		takeNum = treasure.getLimitProductDetail().getOrDefault(request.getRewardId(), 0);
    	}
    	Asserts.isTrue(takeNum < treasureHouseCfg.getLimitNum(), TipsCode.TREASURE_BUY_LIMIT);
    	
    	treasure.getProductDetail().put(request.getRewardId(), takeNum + 1);
    	treasureService.update(treasure);
    	
    	thingService.add(playerContext.getId(), treasureHouseCfg.getReward(), GameCause.TREASURE_FREE_REWARD);
    	
    	TreasureTakeRewardResponse.Builder response = TreasureTakeRewardResponse.newBuilder();
    	response.setShopId(treasure.getShopId());
    	response.setProduct(PbHelper.build(request.getRewardId(), takeNum + 1));
    	return response.build();
    }
}
