/**
 * 
 */
package com.xiugou.x1.battle.buff;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.skill.BuffDamageResult;

/**
 * @author YY
 * 燃烧
 */
public class Burns extends Buff {
	//触发间隔
	private long triggerGapTime;
	
	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.BURNS;
	}

	@Override
	public void onTick(BattleContext context, Sprite sprite) {
		if(context.getNow() >= triggerGapTime) {
			triggerGapTime += this.getBuffConfig().getIntParams().get(1);
			
			BuffDamageResult result = this.calculateBuffDamage(context, sprite);
			sprite.takeBuffDamage(context, result);
		}
	}

	@Override
	protected int getSkillFactor() {
		return this.getBuffConfig().getIntParams().get(0);
	}
}
