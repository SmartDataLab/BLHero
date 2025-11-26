/**
 *
 */
package com.xiugou.x1.game.server.module.task;

import java.util.HashMap;
import java.util.Map;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskType;

import com.xiugou.x1.game.server.module.task.processor.ConditionChangeNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.ConditionsPlusTimesProcessor;
import com.xiugou.x1.game.server.module.task.processor.HaveMeatNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.HaveMineNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.HaveWoodNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.HeroReachLevelProcessor;
import com.xiugou.x1.game.server.module.task.processor.HomeCollectionNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.KillMonsterProcessor;
import com.xiugou.x1.game.server.module.task.processor.KillMonsterTypeProcessor;
import com.xiugou.x1.game.server.module.task.processor.SimpleChangeNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.SimpleCountNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.TroopNumProcessor;
import com.xiugou.x1.game.server.module.task.processor.WearEquipProcessor;

/**
 * @author YY
 */
public enum TaskType implements ITaskType {

	KILL_MONSTER(KillMonsterProcessor.class),//杀死{0}{#}只
	KILL_TYPE_MONSTER(KillMonsterTypeProcessor.class),//杀死{0}{#}只
	LOGIN_FIRST_EVERYDAY(SimpleCountNumProcessor.class),//累计登录{#}天
	LOGIN(SimpleCountNumProcessor.class),//今日登录{#}天
	CAMP_SETUP(SimpleCountNumProcessor.class),//进行露营{#}次
	CAMP_TIME(SimpleCountNumProcessor.class),//进行露营{#}分钟
	RECRUIT_REFRESH(SimpleCountNumProcessor.class),//刷新酒馆{#}次
	RECRUIT_NUM(SimpleCountNumProcessor.class),//伙伴招募{#}次
	HOME_COLLECTION_NUM(HomeCollectionNumProcessor.class),//收集{#}资源
	HOME_COLLECTION_RESOURCE(ConditionsPlusTimesProcessor.class),//收集{0}{#}个
	HERO_UP_LEVEL(SimpleCountNumProcessor.class),//英雄升级{#}次
	HERO_REACH_LEVEL(HeroReachLevelProcessor.class),//{#}个伙伴达到{0}级
	TRAININGCAMP_UP_LEVEL(SimpleCountNumProcessor.class),//训练营升级{#}次
	TREASURE_BOX_OPEN(SimpleCountNumProcessor.class),//打开宝箱{#}次
	RIFTS_CLEAR(null),//清理裂谷{#}次
	UNLOCK_BUILDINGS(ConditionsPlusTimesProcessor.class),//解锁主城{0}建筑
	NPC_INTERACTION(ConditionsPlusTimesProcessor.class),//与{0}NPC交互
	BUILDING_CLEANSING(ConditionsPlusTimesProcessor.class),//建设{0}净化法阵
	BUILDING_TRANSMIT(ConditionsPlusTimesProcessor.class),//建设{0}传送法阵
	COMPLETE_DUPLICATE(ConditionsPlusTimesProcessor.class),//通关{0}副本{#}次
	FORMATION_NUM(SimpleChangeNumProcessor.class),//主线上阵人数
	TOWER_CHALLENGE_NUM(SimpleCountNumProcessor.class),//挑战塔{#}次
	TOWER_CHALLENGE_TIMES(ConditionsPlusTimesProcessor.class),//挑战{0}塔{#}次
	TOWER_FLOOR(ConditionChangeNumProcessor.class),//{0}塔挑战到{#}层
	SALVAGE_EQUIP(SimpleCountNumProcessor.class),//分解装备{#}次
	EXCHANGE_SHOP(SimpleCountNumProcessor.class),//兑换商品{#}次
	HAVE_MINE(HaveMineNumProcessor.class),//当前拥有{#}个矿石
	HAVE_MEAT(HaveMeatNumProcessor.class),//当前拥有{#}个精元
	HAVE_WOOD(HaveWoodNumProcessor.class),//当前拥有{#}个木材
	CHALLENGE_DUNGEONS(ConditionsPlusTimesProcessor.class),//通过{0}秘境副本{#}次
	WEAR_EQUIP(WearEquipProcessor.class),//穿戴装备{#}件
	ACCUMULATE_RECHARGE(SimpleCountNumProcessor.class),//累计充值{#}元
	ROLE_LEVEL(SimpleChangeNumProcessor.class),//角色升级到{#}级
	HAVE_ITEMS(ConditionsPlusTimesProcessor.class),//拥有{#}个{0}道具
	NUMBER_OF_DRAWS(ConditionsPlusTimesProcessor.class),//{0}类型抽奖抽取{#}次
	FIGHTING_NUM(SimpleChangeNumProcessor.class),//达到{#}战力
	TROOP_NUM(TroopNumProcessor.class),//可上阵人数达到{#}
	SHARE(SimpleCountNumProcessor.class),//分享{#}次
	WATCH_ADV(SimpleCountNumProcessor.class),//观看广告{#}次

    ;
	private static Map<String, TaskType> map = new HashMap<>();
	static {
		for(TaskType taskType : TaskType.values()) {
			map.put(taskType.name(), taskType);
		}
	}
	public static TaskType valueFor(String taskIdentity) {
		return map.get(taskIdentity);
	}

    private TaskType(Class<? extends AbstractTaskEventProcessor> processorClazz) {
        AbstractTaskEventProcessor.register(this, processorClazz);
    }

    @Override
    public String identity() {
        return this.name();
    }
}
