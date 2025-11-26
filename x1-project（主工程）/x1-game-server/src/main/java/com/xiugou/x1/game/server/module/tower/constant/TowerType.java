/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.constant;

/**
 * @author YY
 *
 */
public enum TowerType {

	NORMAL(1, "普通塔", 0),
	STRENGTH(2, "力量塔", 1),
	AGILITY(3, "敏捷塔", 2),
	WISDOM(4, "智力塔", 3),
	;
	private final int value;
	private final String desc;
	private final int allowElement;
	
	private TowerType(int value, String desc, int allowElement) {
		this.value = value;
		this.desc = desc;
		this.allowElement = allowElement;
	}
	
	public String getDesc() {
		return desc;
	}

	public int getValue() {
		return value;
	}

	public int getAllowElement() {
		return allowElement;
	}
}
