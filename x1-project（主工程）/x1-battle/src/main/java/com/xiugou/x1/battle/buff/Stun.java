/**
 * 
 */
package com.xiugou.x1.battle.buff;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.buff.status.AbstractStatus;
import com.xiugou.x1.battle.buff.status.StatusEnum;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class Stun extends Buff {

	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.STUN;
	}

	@Override
	public boolean needAddToTarget(BattleContext context, Sprite target) {
		return AbstractStatus.canReachStatus(context, target, StatusEnum.STUN);
	}
	
	@Override
	public void onAddToTarget(BattleContext context, Sprite sprite, Sprite target) {
		target.changeStatusWithEndTime(context, StatusEnum.STUN, this.getEndTime());
	}
}
