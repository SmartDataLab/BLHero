/**
 * 
 */
package com.xiugou.x1.battle.constant;

/**
 * @author YY
 *
 */
public enum BattleResult {
	NONE(0),
	ATK_WIN(1),
	DEF_WIN(2),
	PAUSE(3),
	;
	
	private final int value;
	private BattleResult(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
