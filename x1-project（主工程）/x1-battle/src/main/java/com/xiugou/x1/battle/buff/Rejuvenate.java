/**
 * 
 */
package com.xiugou.x1.battle.buff;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.skill.BuffCureResult;

/**
 * @author YY
 * 持续治疗
 */
public class Rejuvenate extends Buff {

	//触发间隔
	private long triggerGapTime;
	
	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.REJUVENATE;
	}

	@Override
	public void onTick(BattleContext context, Sprite sprite) {
		if(context.getNow() >= triggerGapTime) {
			triggerGapTime += this.getBuffConfig().getIntParams().get(1);
			
			BuffCureResult result = this.calculateBuffCure(context, sprite);
			sprite.takeBuffCure(context, result);
		}
	}

	@Override
	protected int getSkillFactor() {
		return this.getBuffConfig().getIntParams().get(0);
	}
}
