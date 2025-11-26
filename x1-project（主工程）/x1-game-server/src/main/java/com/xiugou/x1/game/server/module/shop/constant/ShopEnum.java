package com.xiugou.x1.game.server.module.shop.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
public enum ShopEnum {
	RECRUIT_POINT(10001,"兑换商店-招募积分商店"),
	EQUIP_MATERIALS_SHOP(10002,"兑换商店-装备材料兑换商店"),
	VILLAGE_POINTS_SHOP(10003,"兑换商店-仙境守卫积分兑换商店"),
	PURGATORY_POINTS_SHOP(10004,"兑换商店-炼狱积分兑换商店"),
	ZBG_DIAMOND_SHOP(10005,"珍宝阁-仙玉充值商店"),
	DAILY_GIFT_SHOP(10006,"每日充值"),
	ZBG_TEHUI_SHOP(10007,"珍宝阁-特惠礼包商店"),
	ZBG_DAILY_SHOP(10008,"珍宝阁-特惠礼包-每日礼包"),
	ZBG_WEEKLY_SHOP(10009,"珍宝阁-特惠礼包-每周礼包"),
	DIAMOND_SHOP(10010,"兑换商店-仙玉兑换商店"),


	;
	private final int shopId;
	private final String shopName;

	private static Map<Integer, ShopEnum> shopEnumMap = new HashMap<>();

	static {
		for (ShopEnum t : ShopEnum.values()) {
			shopEnumMap.put(t.getShopId(), t);
		}
	}
	public static ShopEnum valueOf(int shipId) {
		return shopEnumMap.get(shipId);
	}
	private ShopEnum(int shopId, String shopName) {
		this.shopId = shopId;
		this.shopName = shopName;
	}

	public int getShopId() {
		return shopId;
	}
	public String getShopName() {
		return shopName;
	}
}
