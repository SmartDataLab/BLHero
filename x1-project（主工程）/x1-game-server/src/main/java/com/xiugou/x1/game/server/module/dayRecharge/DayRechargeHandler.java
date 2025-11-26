package com.xiugou.x1.game.server.module.dayRecharge;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.dayRecharge.model.DayRecharge;
import com.xiugou.x1.game.server.module.dayRecharge.service.DayRechargeService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.dayrecharge.DayRecharge.DayRechargeInfoResponse;
import pb.xiugou.x1.protobuf.dayrecharge.DayRecharge.DayRechargeRewardRequest;
import pb.xiugou.x1.protobuf.dayrecharge.DayRecharge.DayRechargeRewardResponse;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Controller
public class DayRechargeHandler extends AbstractModuleHandler {
    @Autowired
    private DayRechargeService dayRechargeService;
    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private ThingService thingService;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        DayRecharge dayRecharge = dayRechargeService.getEntity(playerId);
        
        DayRechargeInfoResponse.Builder response = DayRechargeInfoResponse.newBuilder();
        response.setBuyAll(dayRecharge.isBuyAll());
        response.addAllDayRecharge(dayRecharge.getDayRecharge());
        response.setFreeReward(dayRecharge.isFreeReward());
        response.setBuyRound(dayRecharge.getBuyRound());
        playerContextManager.push(playerId, DayRechargeInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public DayRechargeRewardResponse reward(PlayerContext playerContext, DayRechargeRewardRequest request) {
        DayRecharge dayRecharge = dayRechargeService.getEntity(playerContext.getId());
        Asserts.isTrue(!dayRecharge.isFreeReward(), TipsCode.DAY_RECHARGE_RECEIVED_FREE_REWARD);
        
        dayRecharge.setFreeReward(true);
        dayRechargeService.update(dayRecharge);
        
        thingService.add(playerContext.getId(), battleConstCache.getEveryday_free_reward(), GameCause.EVERYDAY_RECHARGE_FREE_REWARD);
        
        DayRechargeRewardResponse.Builder response = DayRechargeRewardResponse.newBuilder();
        response.setFreeReward(dayRecharge.isFreeReward());
        return response.build();
    }

}
