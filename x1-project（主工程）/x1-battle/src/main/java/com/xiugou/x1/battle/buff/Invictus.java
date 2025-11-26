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
public class Invictus extends Buff {

	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.INVICTUS;
	}

	@Override
	public boolean needAddToTarget(BattleContext context, Sprite buffOwner) {
		return AbstractStatus.canReachStatus(context, buffOwner, StatusEnum.INVICTUS);
	}

	@Override
	public void onAddToTarget(BattleContext context, Sprite caster, Sprite buffOwner) {
		buffOwner.changeStatusWithEndTime(context, StatusEnum.INVICTUS, this.getEndTime());
	}
}
