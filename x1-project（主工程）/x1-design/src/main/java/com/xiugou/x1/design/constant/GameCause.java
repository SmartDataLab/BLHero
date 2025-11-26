/**
 *
 */
package com.xiugou.x1.design.constant;

import org.gaming.prefab.IGameCause;

/**
 * @author YY
 */
public enum GameCause implements IGameCause {

    PLAYER_CHANGE_NAME(200001, "改名"),
    FORCE_CHANGE_NAME(200002, "强制修改名字"),
    GIFTCODE_USE(210001, "使用兑换码"),
    
    
    BATTLE_DROP(300001, "战斗掉落", false),
    BATTLE_ENTER(300002, "进入主线战斗", false),

    MAINLINE_HARVEST(400001, "主线资源收割", false),
    MAINLINE_FOG_OPEN(400002, "迷雾开启"),
    MAINLINE_NPC_OPEN(400003, "NPC开启"),
    MAINLINE_TELEPORT_OPEN(400004, "传送点开启"),
    MAINLINE_FIN_TASK(400005, "完成主线任务", false),
    MAINLINE_DROP(400006, "主线战斗掉落", false),
    MAINLINE_BOX(400007, "领取主线宝箱"),
    MAINLINE_REVIVE(400008, "主线战斗复活"),
    MAINLINE_HANG(400009, "主线离线挂机"),

    BAG_AUTO_USE_ITEM(500001,"自动使用礼包"),
    BAG_RANDOM_USE_ITEM(500002,"随机礼包"),
    BAG_OPTION_ITEM(500003,"自选礼包"),
    BAG_QUICK_BUY(500004,"快捷购买"),

    MAIL_TAKE_REWARD(600001, "领取邮件附件"),
    MAIL_SYS(600002, "后台系统邮件"),

    HERO_ON_CREATE(700001, "创号初始化"),
    HERO_UP_LEVEL(700002, "英雄升级"),
    HERO_AWAKEN(700003, "英雄觉醒"),
    
    GM(800001, "GM命令"),
    GOD_FINGER(800002, "金手指"),
    
    CAMP_ACTIVATE_TALENT(900001, "训练营激活天赋"),

    EQUIP_CHANGE(1200001, "装备变更"),
    EQUIP_SALVAGE(1200002, "装备分解"),
    EQUIP_BAG_LIMIT(1200003, "装备背包超限"),

    DAILY_WEEKLY_ACTIVE_BOX_REWARD(1400001, "日常周常活跃宝箱奖励"),
    DAILY_WEEKLY_TASK(1400002, "日常周常任务奖励"),

    HOME_PRODUCE_POOL_UP_LEVEL(1500001, "家园资源池升级"),
    HOME_PRODUCE_POOL_REWARD(1500002, "家园资源池领取奖励"),
    HOME_PRODUCE_ACTIVATE(1500003, "激活家园资源池"),
    HOME_STORE_UP(1500004, "仓库升级"),

    RECRUIT(1600001, "招募"),
    RECRUIT_REFRESH(1600002, "招募刷新"),
    RECRUIT_ACTIVE(1600003, "招募解锁"),

    GOLDENPIG_PASS(1700001, "黄金猪挑战通关"),
    GOLDENPIG_TICKET(1700002, "黄金猪挑战门票"),

    TOWER_PASS(1800001, "试炼之塔挑战通关"),
    TOWER_REWARD(1800002, "爬塔每日邮件"),
    
    RECHARGE(1900001, "充值"),
    RECHARGE_VIRTUAL(1900002, "虚拟充值GM充值"),
    RECHARGE_VOUCHER(1900003, "代金券充值"),

    
    ZLEXP_BUY_LEVEL(2100101, "经验战令购买等级"),
    ZLEXP_REWARD(2100102, "经验战令购买等级"),
    ZLGOAL_REWARD(2100103, "目标战令领取奖励"),
    
    TONGXINGZHENG_BUY_LEVEL(2100102, "通行证购买等级"),
    TONGXINGZHENG_EXPIRED_REWARD(2100103, "通行证过期奖励"),
    
    TRAIN_GOD_TOKEN_EXPIRED_REWARD(2100201, "修仙令过期奖励"),
    
    TTTTQ_REWARD(2100401, "通天塔特权奖励"),
    XSLB_REWARD(2100501, "限时礼包奖励"),
    CZJJ_REWARD(2100601, "成长基金奖励"),

    QRMB_TASK_REWARD(2100701, "七日目标任务奖励"),
    QRMB_FINAL_REWARD(2100702, "七日目标阶段奖励"),
    
    KFCB_RANK_REWARD(2100801, "伙伴战力榜排名奖励"),
//    ZBZLB_RANK_REWARD(2100901, "装备战力榜排名奖励"),
//    FBZLB_RANK_REWARD(2101001, "法宝战力榜排名奖励"),
    
    ZG_FREE_REWARD(2100901, "直购免费奖励"),
    
    HDMB_TASK_REWARD(2101001, "活动目标任务奖励"),
    
    MRCZ_REWARD(2101101, "每日充值奖励"),
    
    DUNGEON_ENTER(2200001, "地下城进入"),
    DUNGEON_DROP(2200002, "地下城战斗掉落"),
    DUNGEON_PASS(2200003, "地下城挑战通关"),
    DUNGEON_RANK_FINISH(2200004, "副本赛季结算奖励"),

    
    RANK_REWARD(2300001, "排行榜奖励"),
    EXCHANGE_PRODUCT(2400001, "兑换商品"),
    
    
    EVIL_SOUL_REFINE(2500001, "炼制妖魂"),
    EVIL_SOUL_REFINE_REWARD(2500002, "领取炼制妖魂奖励"),
    EVIL_LEVEL_UP(2500003, "妖傀升级"),
    EVIL_SOUL_REFINE_SPEEDUP(2500004, "加速炼制妖魂"),
    EVIL_BUY_SPEEDUP_TIME(2500005, "购买炼妖加速时间"),
    EVIL_ACTIVE(2500006, "妖傀激活"),

    
    PURGATORY_PASS(2600001, "炼狱轮回挑战通过"),
    PURGATORY_BUY_REFINE(2600002, "购买洗炼道具"),
    PURGATORY_BUY_PLUS_TIMES(2600003, "购买洗炼道具"),
    PURGATORY_REFINE(2600004, "洗炼"),
    PURGATORY_CHALLENGE(2600005, "炼狱轮回挑战"),
    PURGATORY_RANK_FINISH(2600006, "炼狱轮回赛季结算奖励"),

    VILLAGE_TICKET(2700001, "村庄保卫门票"),
    VILLAGE_COMPLETE(2700002, "挑战村庄保卫"),
    VILLAGE_SWEEP(2700003, "村庄保卫扫荡"),
    VILLAGE_CHALLENGE(2700004, "村庄保卫挑战"),
    VILLAGE_BUILD(2700005, "村庄保卫建造栅栏"),
    VILLAGE_RANK_FINISH(2700006, "村庄保卫赛季结算奖励"),

    FIRST_RECHARGE_REWARD(2800001, "首充奖励"),
    EVERYDAY_RECHARGE_FREE_REWARD(2900001, "每日首充免费奖励"),
    
    TREASURE_FREE_REWARD(3000001, "珍宝阁免费奖励"),
    
    VIP_BUY_LEVEL_GIFT(3100001, "购买vip等级礼包"),

    SANQI_HUYU_GIFT(3200001, "37互娱发放礼包"),
    SANQI_HUYU_ASK(3200002, "37互娱调查问卷"),
    
    LOGINSIGN_LOGIN(3300001, "登录奖励"),
    LOGINSIGN_SIGN(3300002, "签到奖励"),
    
    PRIVILEGE_NORMAL_DAILY(3400001, "普通特权每日奖励"),
    PRIVILEGE_NORMAL_EXP(3400002, "普通特权经验池奖励"),
    
    
    ACHIEVEMENT_TASK(3700001, "成就任务奖励"),
    ACHIEVEMENT_POINT(3700002, "成就点数奖励"),
    
    ARENA_DAILY_REWARD(3800001, "竞技场每日结算奖励"),
    ARENA_SEASON_REWARD(3800002, "竞技场赛季结算奖励"),
    
    HANDBOOK_LEVEL_UP(3900001, "图鉴升级"),
    HANDBOOK_REWARD(3900002, "图鉴进度奖励"),
    HANDBOOK_SUIT(3900003, "图鉴套装激活"),
    
    
    ;

    private final int code;
    private final String desc;
    //是否可以超限制
    private final boolean overLimit;
    
    private GameCause(int code, String desc, boolean overLimit) {
        this.code = code;
        this.desc = desc;
        this.overLimit = overLimit;
    } 
    private GameCause(int code, String desc) {
        this.code = code;
        this.desc = desc;
        this.overLimit = true;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
    
	public boolean isOverLimit() {
		return overLimit;
	}
}
