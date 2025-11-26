/**
 * 
 */
package com.xiugou.x1.game.server.module.mail.constant;

import org.gaming.prefab.mail.IMailTemplateEnum;

/**
 * @author YY
 *
 */
public enum MailTemplate implements IMailTemplateEnum {
	SYS(1, "系统邮件"),
	TEST(999, "测试邮件"),
	FORCE_CHANGE_NAME(200001, "强制修改角色名"),
	EQUIP_LIMIT(1200001, "装备背包超限"),
	
	TOWER_REWARD(1800001, "爬塔每日奖励"),
	
	PURGATORY_RANK_FINISH(2600001, "炼狱轮回赛季结算邮件"),
	VILLAGE_RANK_FINISH(2700001, "村庄守卫赛季结算邮件"),
	TONGXINGZHENG_EXPIRED_REWARD(2100101, "通行证过期奖励邮件"),
	TRAIN_GOD_TOKEN_EXPIRED_REWARD(2100201, "修仙令过期奖励邮件"),
	SANQI_HUYU_GIFT(3200001, "37互娱发放礼包"),
	SANQI_HUYU_ASK(3200002, "37互娱问卷调查"),
	
	HUO_BAN_ZHAN_LI_BANG(2100801, "伙伴战力榜"),
	ZHUANG_BEI_ZHAN_LI_BANG(2100901, "装备战力榜"),
	FA_BAO_ZHAN_LI_BANG(2101001, "法宝战力榜"),
	
	ARENA_DAILY(3800001, "竞技场每日结算"),
	ARENA_SEASON(3800002, "竞技场赛季结算"),
	
	;
	
	private final int value;
	private final String desc;
	private MailTemplate(int value, String desc) {
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
