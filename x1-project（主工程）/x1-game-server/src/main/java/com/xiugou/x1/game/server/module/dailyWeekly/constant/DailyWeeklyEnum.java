package com.xiugou.x1.game.server.module.dailyWeekly.constant;

/**
 * @author yh
 * @date 2023/6/8
 * @apiNote
 */
public enum DailyWeeklyEnum {
    DAILY(1, "日常"),
    WEEKLY(2, "周常"),
    ;
    private final int  value;
    private final String desc;

    private DailyWeeklyEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getValue() {
        return value;
    }
}
