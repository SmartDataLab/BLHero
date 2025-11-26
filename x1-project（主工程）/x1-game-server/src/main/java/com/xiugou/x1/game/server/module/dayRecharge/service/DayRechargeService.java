package com.xiugou.x1.game.server.module.dayRecharge.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.dayRecharge.model.DayRecharge;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Service
public class DayRechargeService extends PlayerOneToOneResetableService<DayRecharge> {
	
    @Override
    protected DayRecharge createWhenNull(long entityId) {
        DayRecharge dayRecharge = new DayRecharge();
        dayRecharge.setPid(entityId);
        return dayRecharge;
    }

    @Override
    public void doDailyReset(DayRecharge entity){
        entity.setFreeReward(false);
        entity.setBuyAll(false);
        entity.getDayRecharge().clear();
        entity.setBuyRound(1);
    }
}
