package com.xiugou.x1.game.server.module.vip.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/8/28
 * @apiNote 记录所有的特权
 */
public enum VipFuncType {
	MAINLINE_GOLD_RATE(101, "野外挂机杀怪金币加成"),
    MAINLINE_EXP_RATE(102, "野外挂机杀怪经验加成"),
    GOLD_PIG_TIME(103, "黄金猪副本挑战次数"),
    RECRUIT_ALL_TIME(104, "招募一键全抽"),
    OFFLINE_HANG_TIME(105, "开启离线挂机"),
    AD_FREE_TIME(106, "免广告特权"),
    DUNGEON_AUTO_TIME(107, "挑战副本自动战斗"),
    DUNGEON_NEXT_TIME(108, "秘境连续挑战"),
    ARENA_TIME(109, "竞技场挑战次数"),
    HOME_WOOD_RATE(110, "主城木场生产加成"),
    HOME_MINE_RATE(111, "主城矿脉生产加成"),
    HOME_GOLD_RATE(112, "主城钱庄生产加成"),
    ;
    private final int type;
    private final String desc;

    private VipFuncType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private static Map<Integer, VipFuncType> map = new HashMap<>();

    static {
        for (VipFuncType b : VipFuncType.values()) {
            map.put(b.type, b);
        }
    }

    public static VipFuncType valueOf(int type) {
        return map.get(type);
    }

    public int getType() {
        return type;
    }

	public String getDesc() {
		return desc;
	}
}
