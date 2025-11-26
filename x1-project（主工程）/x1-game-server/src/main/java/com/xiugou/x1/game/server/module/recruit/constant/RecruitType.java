/**
 * 
 */
package com.xiugou.x1.game.server.module.recruit.constant;

/**
 * @author YY
 * 招募类型
 */
public enum RecruitType {
	HERO(1),
	;
	
	private final int value;
	
	private RecruitType(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
