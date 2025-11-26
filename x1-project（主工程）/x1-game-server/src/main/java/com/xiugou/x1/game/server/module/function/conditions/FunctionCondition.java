package com.xiugou.x1.game.server.module.function.conditions;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;

/**
 * @author yh
 * @date 2023/6/14
 * @apiNote
 */
public abstract class FunctionCondition {
    public static final Map<OpenfunctionType, FunctionCondition> CONDITION_MAP = new HashMap<>();

    public FunctionCondition() {
    	CONDITION_MAP.put(getOpenfunctionType(), this);
    }

    public static FunctionCondition getFunctionSystem(OpenfunctionType type) {
        return CONDITION_MAP.get(type);
    }

    protected abstract OpenfunctionType getOpenfunctionType();

    public abstract boolean functionOpenOrNot(long pid, int condition);
}
