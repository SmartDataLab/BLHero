package com.xiugou.x1.game.server.module.firstRecharge.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.firstRecharge.model.FirstRecharge;
import com.xiugou.x1.game.server.module.firstRecharge.struct.FirstRechargeData;

import pb.xiugou.x1.protobuf.firstrecharge.FirstRecharge.PbFirstRechargeData;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Service
public class FirstRechargeService extends PlayerOneToOneResetableService<FirstRecharge> {

    @Override
    protected FirstRecharge createWhenNull(long entityId) {
        FirstRecharge firstRecharge = new FirstRecharge();
        firstRecharge.setPid(entityId);
        return firstRecharge;
    }
    
    @Override
	protected void doDailyReset(FirstRecharge entity) {
    	for(FirstRechargeData rechargeData : entity.getRechargeDatas().values()) {
    		if(rechargeData.getRewardDay() >= rechargeData.getCanTakeDay()) {
    			rechargeData.setCanTakeDay(rechargeData.getCanTakeDay() + 1);
        	}
    	}
	}
    
    public PbFirstRechargeData build(FirstRechargeData rechargeData) {
    	PbFirstRechargeData.Builder builder = PbFirstRechargeData.newBuilder();
    	builder.setRechargeId(rechargeData.getRechargeId());
    	builder.setRewardDay(rechargeData.getRewardDay());
    	builder.setCanTakeDay(rechargeData.getCanTakeDay());
    	return builder.build();
    }
}
