/**
 * 
 */
package com.xiugou.x1.battle.constant;

/**
 * @author YY
 *
 */
public enum TargetType {
	SPECIAL(0),
	SELF(1),
	FRIEND(2),
	ENEMY(3),
	;
	
	private final int value;
	private TargetType(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
