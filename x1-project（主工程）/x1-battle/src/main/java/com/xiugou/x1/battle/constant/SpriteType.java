/**
 * 
 */
package com.xiugou.x1.battle.constant;

/**
 * @author YY
 *
 */
public enum SpriteType {
	HERO(1),
	MONSTER(2),
	;
	
	public static SpriteType valueOf(int value) {
		if(value == 1) {
			return HERO;
		} else {
			return MONSTER;
		}
	}
	
	private final int value;
	private SpriteType(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
