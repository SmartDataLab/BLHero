/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

/**
 * @author YY
 *
 */
public interface ISkillLevelConfig {
	//技能ID
	int getSkillId();
	//技能类型，1普攻、2主动、3被动
	int getSkillType();
	
	int getDmgType();
	//目标类型，1自己，2己方，3敌方
	int getTargetType();
	//目标上限
	int getTargetLimit();
	
	//CD时间
	int getCdTime();
	//额外冷却条件
	IBattleKeyV getExtraCd();
	//战斗中被动属性
	List<Attr> getPassiveAttr();
	
	List<BuffTrigger> getBuffs0();
	//技能持续时间
	int getSkillTime();
	//技能持续时间内可打击的次数
	int getStrikeNum();
	
	int getSkillFactor();
	int getFixValue();
}
