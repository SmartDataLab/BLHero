package com.xiugou.x1.game.server.module.function.conditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

/**
 * @author yh
 * @date 2023/8/15
 * @apiNote
 */
@Component
public class OpenDaysCondition extends FunctionCondition {
	
    @Autowired
    private ServerInfoService serverInfoService;

    @Override
    protected OpenfunctionType getOpenfunctionType() {
        return OpenfunctionType.OPEN_DAYS;
    }

    @Override
    public boolean functionOpenOrNot(long pid, int condition) {
    	return serverInfoService.getOpenedDay() >= condition;
    }
}
