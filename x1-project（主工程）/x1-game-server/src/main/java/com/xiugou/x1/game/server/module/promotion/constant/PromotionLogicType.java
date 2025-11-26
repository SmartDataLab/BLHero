/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YY
 *
 */
public enum PromotionLogicType {
	ZHAN_LING_EXP(1001, "经验类战令"),
	ZHAN_LING_GOAL(1002,"目标类战令"),
	
	CHONG_BANG(1008, "冲榜"),
	
	ZHI_GOU(1009, "直购"),
	HUO_DONG_MU_BIAO(1010, "活动目标"),
	MEI_RI_CHONG_ZHI(1011, "每日充值"),
	;
	
	private static Map<Integer, PromotionLogicType> map = new HashMap<>();
	static {
		for(PromotionLogicType type : PromotionLogicType.values()) {
			map.put(type.getValue(), type);
		}
	}
	public static PromotionLogicType valueOf(int value) {
		return map.get(value);
	}
	
	private final int value;
	private final String name;
	private PromotionLogicType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
}
