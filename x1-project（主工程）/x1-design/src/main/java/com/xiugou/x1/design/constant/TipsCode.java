/**
 *
 */
package com.xiugou.x1.design.constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.design.export.ExcelReader;
import org.gaming.design.export.ExcelWriter;
import org.gaming.design.loader.DesignFile;
import org.gaming.prefab.ITipCause;

/**
 * @author YY
 *
 */
public enum TipsCode implements ITipCause {

	DESIGN_NOT_FOUND(10001, "配置未找到"),
	REQUEST_FAILED(10002, "处理请求失败"),
	SYNC_FAIL(10003, "未找到同步协议%s的处理函数"),
	GAME_CONTEXT_NOT_LINK(10004, "目标服务器%s未连接"),
	
    SERVER_NOT_MATCH(200001, "服务器ID不匹配"),
    PLAYER_NOT_READY(200002, "玩家未完成登陆"),
    PLAYER_NOT_EXIST(200003, "玩家%s不存在"),
    OPENID_NOT_EMPTY(200004, "账户ID不能为空"),
    PLAYER_EXIST(200005, "账户ID已被注册"),
    GOLD_LACK(200006, "金币不足"),
    DIAMOND_LACK(200007, "仙玉不足"),
    STRING_EMPTY(200008, "字符串为空"),
    PLAYER_NAME_SENSITIVE_WORD(200009, "%s属于敏感词，请重新输入"),
    SERVER_MAINTAINING(200010, "服务器正在维护中"),
    PLAYER_PRIVILEGE_BUY(200011, "该特权商品已经购买"),
    OPENID_FORBID(200012, "玩家账号被封禁"),
    ERROR_PARAM(200013, "参数%s错误"),
    PLAYER_NAME_HAS_SENSITIVE(200014, "将修改的名字中含有敏感词"),
    REPEATED_NICK(200015, "该昵称已被占用"),
    PLAYER_MAX_LV(200016, "角色等级已满"),
    PLAYER_LOGIN_CHANNEL_FAIL(200017, "登录验证失败"),
    
    CODE_UNUSABLE(210001, "兑换码服务暂时无法使用，请稍后再尝试"),
    CODE_NOT_FOUND(210002, "兑换码无效"),
    CODE_USED(210003, "兑换码已被使用"),
    CODE_USE_SAME_TYPE(210004, "已使用过同类兑换码"),
    CODE_EXPIRE(210005, "兑换码已经过期"),

    BATTLE_TYPE_WRONG(300001, "战斗类型参数错误"),
    BATTLE_CANT_ENTER(300002, "当前无法进入该战斗"),
    BATTLE_HERO_LOCK(300003, "英雄未激活%s"),
    BATTLE_CONTEXT_MISS(300004, "还没有进入战斗"),
    BATTLE_ALLREADY_IN_BATTLE(300005, "已经在该类型的战斗中"),
    BATTLE_NO_NEXT_ZONE(300006, "该场景没有后续区域了"),
    BATTLE_CANT_SWITCH_HERO(300007, "%s战斗无法更换出战英雄"),
    BATTLE_HERO_EMPTY(300008, "出战英雄不能为空"),
    BATTLE_NO_REVIVE(300009, "该战斗复活次数已经用完"),
    BATTLE_HAS_FIN(300010, "该战斗已经结束，无需放弃"),
    BATTLE_HERO_LIMIT(300011, "出战英雄数量超出限制"),
    BATTLE_CANT_REVIVE_TEAM(300012, "%s战斗不能进行团队复活"),
    BATTLE_HERO_CANT_REPEAT(300013, "出战英雄不能重复"),
    BATTLE_POS_CANT_REPEAT(300014, "出战站位不能重复"),
    BATTLE_ZONE_NOT_INIT(300015, "区域%s数据还没有请求初始化"),
    BATTLE_PARAM_MISS(300016, "缺少战斗参数"),

    CAMPING(400001, "当前已经在露营中"),
    NOT_CAMPING(400002, "当前不在露营中"),
    MAINLINE_NO_THE_SCENE(400003, "主线场景未定义该地图%s"),
    MAINLINE_LOCK(400004, "主线场景%s还没有解锁"),
    MAINLINE_CAN_HARVEST(400005, "主线场景中才能进行收割"),
    MAINLINE_NO_HARVEST(400006, "未找到收割物%s"),
    MAINLINE_HARVESTED(400007, "该收割物已经被收获"),
    MEAT_LACK(400008, "肉不足"),
    WOOD_LACK(400009, "木材不足"),
    MINE_LACK(400010, "矿不足"),
    NOT_IN_MAINLINE(400011, "当前不在主线场景中"),
    FOG_OPENED(400012, "该迷雾区域已经开启"),
    TELEPORT_OPENED(400013, "该传送点已经开启"),
    NPC_OPENED(400014, "该NPC已经缴纳完成"),
    MAINLINE_TASK_UNFIN(400015, "该主线任务还没有完成"),
    MAINLINE_TASK_ALLFIN(400016, "该主线任务系列已经全部完成"),
    MAINLINE_STAGE_FRONT(400017, "需先通关该主线场景的前置难度"),
    MAINLINE_NO_GIVEUP(400018, "无法放弃当前的主线挑战"),
    MAINLINE_NO_TYPE_BOX(400019, "没有该类型的宝箱可以领取"),
    MAINLINE_BOX_TIMEOUT(400020, "该宝箱已经消失"),
    MAINLINE_BOX_NOT_REFRESH(400021, "地图宝箱的刷新时间未到"),
    MAINLINE_TASK_ERROR(400022, "主线任务%s数据出错"),
    MAINLINE_TASK_NOT_CURR(400023, "该任务不属于当前主线"),
    MAINLINE_JUMP_TASK_FORWARD(400024, "GM跳任务只能往前跳"),
    MAINLINE_PORTAL_MISS(400025, "回城传送门还未设置"),
    FOG_LEVEL_LIMIT(400026, "还没到达解锁迷雾的等级"),
    CAMP_NO_TIME(400027, "今天已经没有露营时间了"),
    CAMP_ADV_NOT_END(400028, "当前还在高级露营中"),
    CAMP_ADV_NO_NUM(400029, "今天高级露营次数已用光"),

    BAG_ITEM_LACK(500001, "道具不足"),
    GM_PARAME_EMPTY(500002, "GM参数长度不为2"),
    ITEM_TYPE_NOT_USE(500003, "道具不可使用"),
    BAG_OPTION_ITEM(500004, "自选礼包"),
    BAG_ITEM_CANT_USE(500005, "该道具无法使用"),
    BAG_GIFT_CONFIG_ERROR(500006, "礼品组配置错误"),
    BAG_FULL(500007, "背包已满"),
    
    MAIL_NOT_EXIST(600001, "未找到ID为%s的邮件"),
    MAIL_RECEIVES(600002, "附件已领取"),
    MAIL_NO_ATTACHMENT(600003, "该邮件没有附件"),
    MAIL_NO_RECEIVES(600004, "附件未领取"),
    MAIL_UNREAD(600005, "邮件未读"),
    MAIL_CAPACITY_LIMIT(600006,"背包容量已到达上限，请清理背包"),
    MAIL_NO_REWARD(600007, "邮件中没有奖励可以领取"),

    HERO_FREGMENT_LACK(700001, "英雄碎片不足"),
    HERO_NEED_LEVEL(700002, "需要提升玩家等级"),
    HERO_NOT_EXIST(700003, "没有该英雄%s"),
    HERO_AWAKEN_NEED_LV(700004, "该英雄等级还未达到觉醒要求"),
    HERO_FRAGMENT_LACK(700005, "英雄碎片不足"),
    HERO_AWAKEN_PIECE_NOT_SELF(700006, "觉醒中的素材英雄碎片不能是本英雄"),
    HERO_QUALITY_NOT_MATCH(700007, "素材英雄的品质不符合要求"),
    HERO_SAME_HERO_PIECE(700008, "相同英雄不能作为两种素材"),
    HERO_SUB_FRAGMENT_LACK(700009, "素材英雄碎片不足"),
    
    GM_NOT_AVAILABLE(800001, "不能使用GM命令"),
    GM_PARAM_ERROR(800002, "GM命令参数不对"),
    GM_TASK_COMPLETE(800003, "该任务已经完成"),

    COST_IS_EMPTY(900001, "激活天赋消耗出错"),
    LEVEL_NOT_ENOUGH(900002, "玩家等级不足"),

    FORMATION_HEROS_EMPTY(1000001, "布阵英雄为空"),
    FUNCTION_OPEN_FALSE(1100001, "条件未达标，功能开启失败"),
    FUNCTION_NOT_OPEN(1100002, "功能未开启"),


    EQUIP_LACK(1200001, "没有该装备"),
    EQUIP_UNIDENTIFIED(1200002, "该装备未鉴定"),
    EQUIP_IDENTIFIED(1200003, "该装备已鉴定"),
    EQUIP_NO_REQUIRED_IDENTIFIED(1200004, "该装备不需要鉴定"),
    EQUIP_PLAYER_LEVEL_LACK(1200005, "玩家等级不足"),
    EQUIP_TAKE_OFF(1200006, "装备需要先脱下"),
    EQUIP_LOCK(1200007, "装备已锁定，无法分解"),
    EQUIP_SALVAGE_FAIL(1200008, "分解装备失败"),
    EQUIP_DATA_ERROR(1200009, "装备数据错误，未找到ID为%s的装备"),
    EQUIP_WEARING(1200010, "已经穿戴该装备"),
    EQUIP_LIMIT(1200011, "装备背包无法容纳奖励中的所有装备，请清理装备背包"),

    DAILY_ALL_TASK_COMPLETE(1400001,"日常任务已经全部完成"),
    WEEKLY_ALL_TASK_COMPLETE(1400002,"周常任务已经全部完成"),

    HOME_PRODUCE_LEVEL_LACK(1500001, "开启家园产出池等级不足"),
    HOME_BUILDING_ACTIVATED(1500002, "家园建筑已激活"),
    HOME_PRODUCE_UNACTIVATED(1500003, "家园产出池未激活"),
    HOME_PRODUCE_NOT_OPEN(1500004, "家园产出池功能未开启"),
    HOME_PRODUCE_EMPTY(1500005, "当前没有资源可以领取"),
    HOME_PRODUCE_NOT_OPEN_PRECONDITION(1500006, "该建筑未完成前置条件"),
    HOME_UP_STORE_LEVEL_LACK(1500007, "玩家等级不足无法升级仓库"),
    HOME_BUILDING_NOT_POOL(1500008, "该建筑不是资源池"),
    HOME_STORE_CONFIG_MISS(1500009, "仓库升级配置未找到"),
    HOME_PRODUCE_MAX_UP(1500010, "家园资源池已满级"),
    
    
    RECRUIT_FAIL(1600001, "招募随机英雄失败"),
    RECRUIT_POOL_ISEMPTY(1600002, "招募抽取池为空"),
    RECRUIT_REFRESH_WRONG(1600003, "刷新条件不正确"),
    RECRUIT_POINTIPS_LACK(1600004, "招募积分不足"),
    RECRUIT_REFRESH_TYPE_WRONG(1600005, "刷新类型有误"),
    RECRUIT_ACTIVATED(1600006, "酒馆已经激活"),
    RECRUIT_NOT_OPENING(1600007, "功能未开启"),
    RECRUIT_NOT_ACTIVATE(1600008, "酒馆未激活"),
    RECRUIT_INITIAL_MULTIPLE_WRONG(1600009, "初始倍率不正确"),
    RECRUIT_OPEN_MULTIPLE(1600010, "累计召唤10次后开放多倍召唤功能"),
    RECRUIT_SEE_AD_EMPTY(1600011, "广告刷新次数已经用完"),
    RECRUIT_NO_NEED(1600012, "不需要进行这种类型的招募刷新"),

    GOLDENPIG_ONLYONE_ZONE(1700001, "黄金猪挑战只有一个区域"),
    GOLDENPIG_NO_HERO_SWITCH(1700002, "黄金猪挑战中不能更换英雄"),
    GOLDENPIG_NO_REVIVE(1700003, "黄金猪挑战中不能进行团灭复活"),
    GOLDENPIG_CHALLENGE_LIMIT(1700004, "今日黄金猪挑战次数已达上限"),
    GOLDENPIG_CHALLENGE_FRONT(1700005, "需要先通关上一难度才能挑战"),
    GOLDENPIG_CHALLENGING(1700006, "当前已经正在挑战黄金猪"),
    GOLDENPIG_MAX_STAGE(1700007, "没有更高难度的黄金猪挑战"),

    TOWER_ONLYONE_ZONE(1800001, "试炼之塔挑战只有一个区域"),
    TOWER_NO_HERO_SWITCH(1800002, "试炼之塔挑战中不能更换英雄"),
    TOWER_NO_REVIVE(1800003, "试炼之塔挑战中不能进行团灭复活"),
    TOWER_CHALLENGE_FRONT(1800004, "需要先通关上一难度才能挑战"),
    TOWER_CHALLENGING(1800005, "当前已经正在挑战试炼之塔"),
    TOWER_NEED_ELEMENT(1800006, "该试炼之塔挑战只能上阵指定种族的英雄"),
    TOWER_NO_CHALLENGE(1800007, "该塔在当前时间不可挑战"),

    RECHARGE_SERVICE_MISS(1900001, "充值商品%s配置错误"),
    
    PROMOTION_NOT_FOUND(2100001, "未找到%s的活动"),
    PROMOTION_NOT_YOURS(2100002, "该活动不在你所在的服务器进行"),
    PROMOTION_NOT_MATCH(2100003, "活动%s类型错误"),
    PROMOTION_NOT_RUNNING(2100004, "活动%s不在进行中"),
    PROMOTION_NOT_OPENING(2100005, "活动%s不在开启中"),
    
    ZL_NO_REWARD(2100101, "当前战令没有奖励可以领取"),
    ZL_BUY_HIGH(2100102, "该战令已购买高级奖品"),
    ZL_MAX_LEVEL(2100103, "该战令已满级"),
    
    TXZ_HAS_BUY(2100103, "已经购买该通行证"),
    TXZ_EXCEED_MAX_LEVEL(2100104, "超出最大等级"),
    
    TRAIN_GOD_TOKEN_OPEN(2100301,"本期修仙令已开通"),
    TRAIN_GOD_PARAM_TRANSMIT_WRONG(2100301,"开通修仙令时传递参数错误"),

    TTTTQ_ERROR_PARAM(2100401, "塔类型参数错误%s"),
    TTTTQ_NO_REWARD(2100402, "当前没有通天塔特权奖励可以领取"),
    TTTTQ_VIP_LACK(2100403, "还没有达到购买该通天塔特权奖励的VIP等级要求"),
    TTTTQ_ROUND_LOCK(2100404, "该商品不在本期活动中"),
    TTTTQ_BOUGHT(2100405, "你已经购买该商品"),

    XSLB_NOT_EMPTY_CONDITION(2100501,"为满足该礼包购买条件"),
    XSLB_EXPIRED(2100502,"该限时礼包已经超时"),
    XSLB_HERO_LEVEL_LACK(2100503,"限时礼包英雄等级不够"),
    XSLB_FOG_NOT_OPENED(2100504,"限时礼包该区域迷雾未开启"),

    CZJJ_BOUGHT(2100601, "你已经购买本期成长基金"),
    CZJJ_ROUND_LOCK(2100602, "只能购买本期商品"),
    CZJJ_TOOK_REWARD(2100603, "已经领取该奖励"),
    CZJJ_LEVEL_LACK(2100604, "你的等级还没有达到领取奖励的要求"),
    
    QRMB_TASK_NULL(2100701, "未找到该七日目标任务%s"),
    QRMB_TASK_NOT_FIN(2100702, "该七日目标任务还没有完成"),
    QRMB_TASK_REWARD_TOOK(2100703, "该七日目标任务奖励已经领取"),
    QRMB_FINAL_NOT_FIN(2100704, "该七日目标阶段奖励目标还没有完成"),
    QRMB_FINAL_REWARD_TOOK(2100705, "该七日目标阶段奖励已经领取"),
    
    ZG_BUY_LIMIT(2100901, "该商品已售罄"),
    ZG_TYPE_ERROR(2100902, "该商品类型错误"),
    
    HDMB_TASK_ERROR(2101001, "未找到该任务数据"),
    HDMB_TASK_UN_DONE(2101002, "该任务还没有完成"),
    HDMB_TASK_EMPTY(2101003, "该任务奖励已经领取"),
    
    MRCZ_REWARD_TOOK(2101101, "该奖励已经领取"),
    MRCZ_REWARD_LOCK(2101102, "还没有达到领取该奖励的条件"),

    DUNGEON_ZONE_OVER(2200001,"该地下城区域已完结"),
    DUNGEON_ZONE_PASS(2200002,"该主线地下城已通过"),
    DUNGEON_TICKEN_LACK(2200003,"该地下城门票不足"),
    DUNGEON_PRECONDITION_NOT_COMPLETED(2200004,"上一关卡未完成"),
    DUNGEON_SETTLEMENT_TIME(2200005,"副本处于赛季结算时间"),
    DUNGEON_ZONE_WRONG(2200006,"请按顺序挑战副本"),
    DUNGEON_ZONE_NOT_CLEAR(2200007,"还没有完全击败副本中的怪物"),

    RANK_NO_DATA(2300001,"该排行榜没有数据"),
    RANK_SELF_NO_DATA(2300002,"该排行榜自身数据丢失"),
    RANK_RECEIVED_REWARD(2300003,"该排行榜奖励已领取"),
    RANK_NO_RECEIVED_REWARD(2300004,"该排行榜奖励未达成"),
    RANK_TYPE_MISS(2300005,"该排行榜类型错误"),
    
    FAIRY_PEARL_LACK(2400001,"成仙珠不足"),
    EQUIP_MATERIALS_LACK(2400002,"装备积分不足"),
    EXCHANGE_POINTS_LACK(2400003,"兑换积分不足"),
    PURGATORY_POINTS_LACK(2400004,"炼狱积分不足"),
    EXCHANGE_ACHIEVE_LIMIT(2400005,"兑换数量达到上限"),
    EXCHANGE_SHOP_ID_WRONG(2400006,"商店ID不正确%s"),
    EXCHANGE_PRODUCT_ID_WRONG(2400007,"商品ID不正确%s"),
    EXCHANGE_PRODUCT_CANNOT_BUY(2400008,"商品不可购买"),
    EXCHANGE_CONDITION_INCORRECT(2400009,"条件不达标"),
    EXCHANGE_NOT_RIGHT_ROUND(2400010,"商定期数不匹配"),

    EVI_FRAGMENT_LACK(2500001,"妖碎片不足"),
    EVI_FURNACE_ENOUGH(2500002,"炼妖炉已满"),
    EVI_FURNACE_UNDONE(2500003,"炼妖未结束"),
    EVI_IDENTITY_WRONG(2500004,"妖傀ID有误"),
    EVI_FURNACE_EMPTY(2500005,"没有妖魂在炼制"),
    EVI_MAX_LEVEL(2500006,"该妖已经满级"),
    EVI_SPEEDUP_TIME_LACK(2500007,"炼妖加速已用完"),
    EVI_POSITION_FURNACE_EMPTY(2500008,"该位置没有正在炼制的妖魂"),
    EVI_POS_ERROR(2500009, "炼妖位置参数错误"),
    EVI_NO_RUNNING(2500010, "当前没有正在进行的炼妖"),
    EVI_SPEEDUP_TIME_UNSPENT(2500008,"炼妖加速没用完"),

    PURGATORY_ZONE_OVER(2600001,"该炼狱区域已探索完成"),
    PURGATORY_BEFORE_LEVEL_UNDONE(2600002,"上一阶未通过"),
    PURGATORY_HAVE_FREE_TIMES(2600003,"还有免费洗炼次数"),
    PURGATORY_LEVEL_NOT_COMPLETE(2600004,"当前关卡未通过"),
    PURGATORY_PLUS_TIMES_EMPTY(2600005,"加成次数已用完"),
    PURGATORY_ZONE_WRONG(2600006,"请按顺序挑战副本"),


    VILLAGE_ONLY_ONE_ZONE(2700001, "仙境守卫只有一个区域"),
    VILLAGE_TICKET_EMPTY(2700002, "仙境守卫门票不足"),
    VILLAGE_LEVEL_UNFINISHED(2700003, "当前关卡未通过"),
    VILLAGE_POINT_LACK(2700004, "仙境守卫积分不足"),
    VILLAGE_BUILD_LIMIT(2700005, "仙境守卫防御建筑到达上限"),
    FIRST_RECHARGE_HAS_BUY(2800001, "已经完成首充"),
    FIRST_RECHARGE_NO_BUY(2800002, "没有首充"),
    FIRST_RECHARGE_RECEIVED_REWARD(2800003, "已领取当天首充奖励"),
    FIRST_RECHARGE_REWARD_WRONG(2800004, "需要先领取前一天的奖励"),
    FIRST_RECHARGE_RECEIVED_TODAY_REWARD(2800005, "今天奖励已经领取"),

    DAY_RECHARGE_REWARD_WRONG(2900001, "不可重复购买"),
    DAY_RECHARGE_RECEIVED_FREE_REWARD(2900002, "免费好礼已经领取"),
    DAY_RECHARGE_NEED_FIRST_RECHARGE(2900003, "首充后解锁"),
    DAY_RECHARGE_ROUND_WRONG(2900004, "还没有达到购买该轮商品的条件"),
    
    TREASURE_BUY_CONDITION_SATISFY(3000001, "未满足购买条件"),
    TREASURE_BUY_LIMIT(3000002, "到达购买上限"),
    TREASURE_CHOOSE_WRONG(3000003, "未选择自选商品"),

    VIP_LEVEL_NOT_ENOUGH(3100001, "vip等级不足"),
    VIP_GIFT_ALREADY_BOUGHT(3100002, "该vip等级礼包已购买"),
    
    SANQIHY_ASK_EMPTY(3200001, "该调查问卷奖励已领取"),

    LOGINSIGN_LOGIN_REWARD_TOOK(3300001, "登陆天数不足"),
    LOGINSIGN_SIGN_REWARD_TOOK(3300002, "今天的签到奖励已经领取"),
    LOGINSIGN_LOGIN_REWARD_MAX(3300003, "已经没有更多的登录奖励可领取"),
    
    PRIVILEGE_NORMAL_NOT_CHOOSE(3400001, "还没有挑选自选道具"),
    PRIVILEGE_NORMAL_NOT_END(3400002, "该特权还没有失效"),
    PRIVILEGE_NORMAL_NOT_OPEN(3400003, "该特权还没有开通"),
    PRIVILEGE_NORMAL_TOOK(3400004, "今日的特权奖励已经领取"),
    PRIVILEGE_NORMAL_EXP_NOT_FULL(3400005, "特权经验池还没有蓄满"),
    
    CHAT_HAS_SENSITIVE(3600001, "聊天信息含有敏感信息"),
    CHAT_CHANNEL_MISS(3600002, "未找到该聊天频道%s"),
    
    ACHIEVEMENT_TASK_REWARD_TOOK(3700001, "该成就奖励已经领取"),
    ACHIEVEMENT_TASK_UN_DONE(3700002, "该成就还没有达成"),
    ACHIEVEMENT_POINT_NO_MORE(3700003, "已经没有更多的成就点数奖励"),
    ACHIEVEMENT_POINT_LACK(3700004, "还没有达到领取该奖励的成就点数"),
    
    ARENA_REFRESH_CD(3800001, "刷新还在冷却中"),
    ARENA_COMPETITOR_MISS(3800002, "该玩家不在你的挑战列表中"),
    ARENA_FIGHTER_NULL(3800003, "挑战信息已失效，请重新开始挑战"),
    
    HANDBOOK_NO_ELEMENT(3900001, "该图鉴元素不在该图鉴下"),
    HANDBOOK_MAX_LV(3900002, "该图鉴已经满级"),
    HANDBOOK_LEVEL_UP_UNREACH(3900003, "还没有达到提升该图鉴的要求"),
    HANDBOOK_MAX_PROCESSOR(3900004, "没有更多的进度积分奖励可以领取了"),
    HANDBOOK_POINT_LACK(3900005, "还没有达到领取该奖励的积分要求"),
    HANDBOOK_REWARD_LIMIT(3900006, "还没有达到领取该奖励的条件"),
    HANDBOOK_REWARD_TOOK(3900007, "该图鉴奖励已经领取"),
    HANDBOOK_SUIT_ACTIVED(3900008, "该图鉴套装已经激活"),
    HANDBOOK_SUIT_LACK(3900009, "还没有达到激活该套装的条件"),
    
    ;
	
	public static Map<Integer, TipsCode> codes = new HashMap<>();
	static {
		for (TipsCode code : TipsCode.values()) {
			codes.put(code.getCode(), code);
		}
	}
	public static TipsCode valueOf(int code) {
		return codes.get(code);
	}
	
    private final int code;
    private final String desc;

    private TipsCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    
	public static void main(String[] args) {
		String filePath = "D:\\Git\\tubing\\tubing-x1-config\\excel";
		String fileName = "YLanguageTips.xlsx";
		File file = new File(filePath + File.separator + fileName);
		ExcelReader excelReader = new ExcelReader();
		DesignFile designFile = excelReader.read(file).get(0);

		Map<String, String[]> map = new HashMap<>();

		for (String[] datas : designFile.getDatas()) {
			map.put(datas[1], datas);
		}

		Map<Integer, String[]> resultMap = new HashMap<>();

		for (TipsCode tipsCode : TipsCode.values()) {
			String key = tipsCode.getCode() + "";
			String[] datas = map.get(key);
			if (datas == null) {
				datas = new String[3];
			}
			datas[1] = key;
			datas[2] = tipsCode.getDesc();
			resultMap.put(tipsCode.getCode(), datas);
		}
		List<Integer> keys = new ArrayList<>(resultMap.keySet());
		Collections.sort(keys);

		designFile.getDatas().clear();
		for (int i = 0; i < keys.size(); i++) {
			int key = keys.get(i);
			String[] datas = resultMap.get(key);

			datas[0] = (i + 1) + "";
			designFile.getDatas().add(datas);
			System.out.println(Arrays.toString(datas));
		}

		ExcelWriter.write(filePath, fileName, designFile);
		System.out.println("导出完成");
	}
}
