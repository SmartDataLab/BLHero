/**
 * 
 */
package com.xiugou.x1.game.server.module.battle.constant;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.battle.IBattleType;

/**
 * @author YY
 *
 */
public enum BattleType implements IBattleType {
	
	MAINLINE(1, "主线场景", false),
	TOWER(2, "塔", true),
	TOWER_OF_STRENGTH(3, "力量之塔", true),
	TOWER_OF_AGILITY(4, "敏捷之塔", true),
	TOWER_OF_WISDOM(5, "智慧之塔", true),
	GOLDEN_PIG(6, "金猪", true),
	PURGATORY(7, "炼狱轮回", true),//时空裂缝
	VILLAGE_DEFENSE(8, "村庄保卫", true),
	DUNGEON(9, "地下城", true),
	ARENA(10, "竞技场（进攻阵容）", true),
	ARENA_DEF(11, "竞技场（防守阵容）", true),
	;
	
	private static Map<Integer, BattleType> map = new HashMap<>();
	static {
		for(BattleType battleType : BattleType.values()) {
			map.put(battleType.value, battleType);
		}
	}
	public static BattleType valueOf(int value) {
		return map.get(value);
	}
	
	private final int value;
	private final String desc;
	private final boolean cleanOnLogout;
	
	private BattleType(int value, String desc, boolean cleanOnLogout) {
		this.value = value;
		this.desc = desc;
		this.cleanOnLogout = cleanOnLogout;
	}
	
	public String getDesc() {
		return desc;
	}

	public int getValue() {
		return value;
	}

	public boolean isCleanOnLogout() {
		return cleanOnLogout;
	}
}
