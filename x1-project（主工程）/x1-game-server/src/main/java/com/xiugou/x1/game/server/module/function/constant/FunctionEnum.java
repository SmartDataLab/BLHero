package com.xiugou.x1.game.server.module.function.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/6/27
 * @apiNote
 */
public enum FunctionEnum {
	BAG(10001,"背包"),
	WORLD_MAP(10002,"世界地图"),
	SHOP(10003,"商城"),
	TASK(10004,"任务"),
	CAMP(10005,"修行"),
	COME_BACK(10006,"回城(回归)"),
	SETTING(10007,"设置"),
	RI_CHANG(10008,"日常任务"),
	MAIL(10009,"信件"),
	PROTECT_GOD(10010,"神殿"),
	EXCHANGE_SHOP(10011,"兑换商店"),
	PUB(10012,"神像"),
	DUNGEON_ENTRY(10013,"秘境(副本入口)"),
	MAIN_TOWN_TRANSFER(10014,"瞬移"),
	TRAINING_CAMP(10015,"神坛"),
	HERO_HeroManage(10016,"伙伴系统"),
	HERO_ARRAY(10017,"伙伴出阵"),
	BANK(10019,"钱庄"),
	MINE_AREA(10020,"灵脉"),
	LOGGING_CAMP(10021,"伐木场"),
	STORER(10022,"仓库"),
	RANK(10023,"排行榜"),
	DAILY_DUP(10024,"日常秘境"),
	SKY_TOWER(10025,"通天塔"),
	HUMAN_TOWER(10026,"人族塔"),
	SPRITE_TOWER(10027,"灵族塔"),
	FAIRY_TOWER(10028,"仙族塔"),
	GOLDEN_PIG(10029,"黄金洞窟"),
	FAIRYPALACE_DEFEND(10030,"仙殿防卫"),
	PURGATORY_SAMSARA(10031,"炼狱轮回"),
	CHALLENGE(10032,"挑战入口"),
	HERO_SHOP(10033,"神像商店"),
	EQUIP_SHOP(10034,"装备商店"),
	PURGATORY_SHOP(10035,"炼狱商店"),
	FAIRYPALACE_SHOP(10036,"仙境商店"),
	XIANZHU_SHOP(10037,"辰仙珠商店"),
	DEMON_Forge(10038,"炼妖"),
	ELEMENT_EQUIP1(10039,"种族装备1"),
	ELEMENT_EQUIP2(10040,"种族装备2"),
	ELEMENT_EQUIP3(10041,"种族装备3"),
	FORMATION(10042,"上阵"),
	ZHENBAOGE_ENTRY(10043,"珍宝阁入口"),
	XIANYU_RECHARGE(10044,"仙玉充值"),
	TEHUI_RECHARGE(10045,"特惠礼包"),
	MEIZHOU_RECHARGE(10046,"每周礼包"),
	DINGZHI_RECHARGE(10047,"定制礼包"),
	FIRST_RECHARGE(10048,"首充"),
	MEIRI_RECHARGE(10049,"每日礼包"),
	LOGIN_PACK(10050,"七天登录"),
	SIGN_PACK(10051,"签到"),
	PROBLEM_SURVEY(10052,"问卷调查"),
	WECHAT_GROUP(10053,"微信群"),
	SEVEN_DAY_GOAL(10054,"七日目标"),
	OFFLINE_BENEFITS(10055,"离线挂机"),
    
    ;
//10013	副本入口
//10014	瞬间移动
//10017	英雄出阵
//10018	挑战副本

    private final int functionId;
    private final String functionName;

    private FunctionEnum(int functionId, String functionName) {
        this.functionId = functionId;
        this.functionName = functionName;
    }

    public int getFunctionId() {
        return functionId;
    }

    public String getFunctionName() {
        return functionName;
    }


    private static Map<Integer, FunctionEnum> map = new HashMap<>();

    static {
        for (FunctionEnum functionEnum : FunctionEnum.values()) {
            map.put(functionEnum.getFunctionId(), functionEnum);
        }
    }

    public static FunctionEnum valueOf(int functionId) {
        return map.get(functionId);
    }
}
