package com.xiugou.x1.game.server.module.trainingCamp.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/6/16
 * @apiNote
 */
public enum TrainingCampType {
    GENERAL(1, "普通训练营"),
    EXPERT(2, "高级训练营"),
    ;


    private final int code;
    private final String desc;

    private TrainingCampType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }


    private static Map<Integer, TrainingCampType> map = new HashMap<>();

    static {
        for (TrainingCampType t : TrainingCampType.values()) {
            map.put(t.code, t);
        }
    }

    public static TrainingCampType valueOf(int code) {
        return map.get(code);
    }

	public String getDesc() {
		return desc;
	}
}
