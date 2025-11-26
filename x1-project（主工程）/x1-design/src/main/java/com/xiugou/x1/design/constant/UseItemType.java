package com.xiugou.x1.design.constant;

/**
 * @author yh
 * @date 2023/7/31
 * @apiNote
 */
public enum UseItemType {
	RANDOM_ITEM(1, "随机道具"),
	OPTIONAL_ITEM(2, "自选道具"),

	;
	private final int id;
	private final String desc;

	private UseItemType(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public static UseItemType valueOf(int id) {
		for (UseItemType type : UseItemType.values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}
}
