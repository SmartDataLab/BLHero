/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

/**
 * @author YY
 *
 */
public interface IBattleConstCache {
	//普攻伤害系数
	int getFinalNormalFactor();
	//技能伤害系数
	List<Integer> getFinalSkillFactor();
	//命中率区间
	List<Integer> getHitRange();
	//暴击率区间
	List<Integer> getCritRange();
	//伤害浮动系数
	int getDmgFloat();
	
	List<Attr> getMonsterInitAttr();
}
