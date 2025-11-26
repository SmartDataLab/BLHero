/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.constant;

/**
 * @author YY
 *
 */
public enum ProductType {
	NORMAL(1, "普通充值"),
	TONG_XING_ZHENG(2, "通行证"),
	ADV_PRIVILEGE(3, "永久去广告"),	//废弃
	EVERYDAY_RECHARGE(4, "每日充值"),
	TREASURE_RECHARGE(5, "珍宝阁充值"),
	TRAIN_GOD_TOKEN(6, "修仙令"),
	TONG_TIAN_TA_TE_QUAN(7, "通天塔特权商品"),
	XIAN_SHI_LI_BAO(8,"限时礼包"),
	CHENG_ZHANG_JI_JIN(9, "成长基金"),
	FIRST_RECHARGE(10, "首充"),
	PRIVILEGE_NORMAL(11, "特权直购"),
	ZHI_GOU(12, "直购活动商品"),
	ZHAN_LING_EXP(13, "经验类战令"),
	ZHAN_LING_GOAL(14, "目标类战令"),
	;
	private final int value;
	private ProductType(int value, String desc) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
