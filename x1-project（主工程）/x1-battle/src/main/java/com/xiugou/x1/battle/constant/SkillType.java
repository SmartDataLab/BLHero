/**
 * 
 */
package com.xiugou.x1.battle.constant;

/**
 * @author YY
 *
 */
public enum SkillType {
	NORMAL(1),
	ACTIVE(2),
	PASSIVE(3),
	;
	
	private final int value;
	private SkillType(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
