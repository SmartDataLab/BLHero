/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.struct;

/**
 * @author YY
 *
 */
public enum ServerType {
	TEST(1, "测试"),
	REVIEW(2, "审核"),
	NORMAL(3, "正式"),

	;
	private final int value;
	private final String desc;

	private ServerType(int value, String desc) {
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
