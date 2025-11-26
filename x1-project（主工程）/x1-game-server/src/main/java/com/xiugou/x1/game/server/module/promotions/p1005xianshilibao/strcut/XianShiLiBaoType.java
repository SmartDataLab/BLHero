/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.strcut;

/**
 * @author YY
 *
 */
public enum XianShiLiBaoType {
	ALL(0, "全礼包"),
	LEVEL(1, "等级礼包"),
	HERO_LEVEL(2, "英雄等级礼包"),
	MAINLINE_TASK(3, "主线任务礼包"),
	HERO_GET(4, "获得伙伴礼包"),//暂时废弃
	GOLD(5, "金币礼包"),
	TOWER(6, "通天塔礼包"),
	MAINLINE_DEAD(7, "主线野外死亡礼包"),
	DIAMOND_RECRUIT(8, "仙玉招募礼包"),
	ML_DUNGEON_DEAD(9, "主线副本死亡礼包"),
	DUNGEON_DEAD(10, "秘境副本死亡礼包"),
	TOWER_DEAD(11, "爬塔死亡礼包"),
	GOLDENPIG_DEAD(12, "黄金猪死亡礼包"),
	;
	private final int value;
	private final String desc;
	private XianShiLiBaoType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	public int getValue() {
		return value;
	}
	public String getDesc() {
		return desc;
	}
}
