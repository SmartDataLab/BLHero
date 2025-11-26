/**
 * 
 */
package com.xiugou.x1.design.constant;

/**
 * @author YY
 *
 */
public enum HeroElement {
	STRENGTH(1, "力量"),
	AGILITY(2, "敏捷"),
	INTELLECT(3, "智力"),
	;
	
	private final int value;
	private final String desc;
	private HeroElement(int value, String desc) {
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
