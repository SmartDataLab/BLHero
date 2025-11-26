/**
 * 
 */
package com.xiugou.x1.battle.constant;

/**
 * @author YY
 *
 */
public enum BuffTarget {
	SELF(1),
	TARGET(2),
	FRIEND(3),
	RANDOM_FRIEND(4),
	;
	
	private final int value;
	private BuffTarget(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
