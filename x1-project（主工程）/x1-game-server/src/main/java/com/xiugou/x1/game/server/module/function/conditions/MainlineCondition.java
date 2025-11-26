package com.xiugou.x1.game.server.module.function.conditions;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import org.springframework.stereotype.Component;

/**
 * @author yh
 * @date 2023/6/14
 * @apiNote
 */
@Component
public class MainlineCondition extends FunctionCondition {
	
    @Override
    protected OpenfunctionType getOpenfunctionType() {
        return OpenfunctionType.MAINLINE_OPEN;
    }
    
    @Override
    public boolean functionOpenOrNot(long pid, int condition) {
        //这个条件已经废弃，条件判断代码先保留
        return true;
    }
}
