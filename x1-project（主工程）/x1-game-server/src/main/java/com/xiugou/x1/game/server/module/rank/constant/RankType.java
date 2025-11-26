/**
 *
 */
package com.xiugou.x1.game.server.module.rank.constant;

/**
 * @author YY
 */
public enum RankType {
	LEVEL(1, "等级排行榜", 0, 0),
	FIGHTING(2, "主线战力排行榜", 0, 0),
	HERO(3, "英雄基础战力排行榜", 0, 0),
	EQUIP(4, "装备排行榜", 0, 0),
	PURGATORY(5, "炼狱轮回阶数排行榜", 0, 0),
	VILLAGE(6, "村庄保卫关卡排行榜", 0, 0),
	FOG(7, "迷雾解锁排行榜", 0, 0),
	HERO_RECRUIT(8, "伙伴招募排行榜，活动", 0, 1008101),
	EQUIP_RECRUIT(9, "装备招募排行榜，活动", 0, 1008102),
	FABAO_RECRUIT(10, "法宝招募排行榜，活动", 0, 1008103),
	TOWER_NORMAL(11, "普通塔排行榜", 0, 0),
	
	;

	private final int value;
	private final int limit;
	private final String desc;
	//活动ID
	private final int activityId;

	private RankType(int value, String desc, int limit, int activityId) {
		this.value = value;
		this.desc = desc;
		this.limit = limit;
		this.activityId = activityId;
	}

	public int getValue() {
		return value;
	}

	public static RankType getRankType(int value) {
		for (RankType rankType : RankType.values()) {
			if (value == rankType.value) {
				return rankType;
			}
		}
		return null;
	}

	public int getLimit() {
		return limit;
	}

	public String getDesc() {
		return desc;
	}

	public int getActivityId() {
		return activityId;
	}
}
