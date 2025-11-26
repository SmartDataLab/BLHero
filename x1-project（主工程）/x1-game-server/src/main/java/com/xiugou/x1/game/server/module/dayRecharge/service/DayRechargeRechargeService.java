package com.xiugou.x1.game.server.module.dayRecharge.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.DayRechargeCache;
import com.xiugou.x1.design.module.autogen.DayRechargeAbstractCache.DayRechargeCfg;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.dayRecharge.model.DayRecharge;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.dayrecharge.DayRecharge.DayRechargeBuyMessage;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Service
public class DayRechargeRechargeService extends RechargeOrderingService {
	
    @Autowired
    private DayRechargeService dayRechargeService;
    @Autowired
    private DayRechargeCache dayRechargeCache;

    @Override
    public ProductType productType() {
        return ProductType.EVERYDAY_RECHARGE;
    }

    @Override
    public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
        DayRecharge dayRecharge = dayRechargeService.getEntity(playerId);
        DayRechargeCfg dayRechargeCfg = dayRechargeCache.getInProductIdIndex(rechargeProductCfg.getId());
        Asserts.isTrue(dayRechargeCfg.getRound() == dayRecharge.getBuyRound(), TipsCode.DAY_RECHARGE_ROUND_WRONG);
        
        if (Integer.parseInt(rechargeProductCfg.getProductParam()) == 1) {
            Asserts.isTrue(!dayRecharge.isBuyAll(), TipsCode.DAY_RECHARGE_REWARD_WRONG);
        } else {
            Asserts.isTrue(!dayRecharge.getDayRecharge().contains(rechargeProductCfg.getId()), TipsCode.DAY_RECHARGE_REWARD_WRONG);
        }
    }

    @Override
    public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
        RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(recharge.getProductId());
        DayRecharge dayRecharge = dayRechargeService.getEntity(playerId);
        if (Integer.parseInt(rechargeProductCfg.getProductParam()) == 1) {
            dayRecharge.setBuyAll(true);
        } else {
            dayRecharge.getDayRecharge().add(recharge.getProductId());
        }
        if(dayRecharge.isBuyAll() || dayRecharge.getDayRecharge().size() >= dayRechargeCache.getInRoundCollector(dayRecharge.getBuyRound()).size() - 1) {
        	if(dayRechargeCache.findInRoundCollector(dayRecharge.getBuyRound() + 1) != null) {
        		dayRecharge.setBuyRound(dayRecharge.getBuyRound() + 1);
        		dayRecharge.setBuyAll(false);
        		dayRecharge.getDayRecharge().clear();
        	}
        }
        dayRechargeService.update(dayRecharge);
        
        DayRechargeBuyMessage.Builder builder = DayRechargeBuyMessage.newBuilder();
        builder.setBuyAll(dayRecharge.isBuyAll());
        builder.addAllDayRecharge(dayRecharge.getDayRecharge());
        builder.setBuyRound(dayRecharge.getBuyRound());
        playerContextManager.push(playerId, DayRechargeBuyMessage.Proto.ID, builder.build());
    }
}
