package com.xiugou.x1.game.server.module.function.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/6/7
 * @apiNote
 */
public enum OpenfunctionType {
    PLAYER_LEVEL(1, "玩家等级"),
    MAINLINE_OPEN(2, "主线地图开启"),
    TRAINING_ADVANCED_CAMP_LEVEL(3, "训练营高级等级"),
    OPEN_DAYS(4, "开服天数"),
    DUNGEON(5, "通关副本"),
    MAINLINE_TASK(6, "完成主线任务"),
    ;

    private int code;
    private String desc;

    private OpenfunctionType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }



    private static Map<Integer, OpenfunctionType> map = new HashMap<>();

    static {
        for (OpenfunctionType open : OpenfunctionType.values()) {
            map.put(open.getCode(), open);
        }
    }

    public static OpenfunctionType valueOf(int type) {
        return map.get(type);
    }




}
