/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 * 被动技能
 */
public abstract class PassiveSkill<S> extends Skill<S> {

	//进入战斗时触发
	public void onEnterBattle(BattleContext context, Sprite sprite) {
		
	}
	
	
}
