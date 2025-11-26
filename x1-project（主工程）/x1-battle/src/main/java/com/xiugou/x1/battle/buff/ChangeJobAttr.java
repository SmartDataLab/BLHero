/**
 * 
 */
package com.xiugou.x1.battle.buff;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class ChangeJobAttr extends Buff {

	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.CHANGE_JOB_ATTR;
	}

	@Override
	public boolean needAddToTarget(BattleContext context, Sprite target) {
		if(target.getElement() == this.getBuffConfig().getIntParams().get(0)) {
			return true;
		}
		return false;
	}
}
