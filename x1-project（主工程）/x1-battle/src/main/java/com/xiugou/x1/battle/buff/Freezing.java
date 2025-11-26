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
 * 冻伤
 */
public class Freezing extends Buff {
	//层数
	private int layer;
	//是否已冰冻
	private boolean hasFreezed = false;
	
	@Override
	public BuffEffect buffEffect() {
		return BuffEffect.FREEZING;
	}
	
	@Override
	public boolean needAddToTarget(BattleContext context, Sprite target) {
		//没有冰冻过才需要叠加到目标身上
		return !hasFreezed;
	}

	@Override
	public void onAddToTarget(BattleContext context, Sprite sprite, Sprite target) {
		layer += 1;
		if(!hasFreezed && layer >= this.getBuffConfig().getIntParams().get(0)) {
			//改变敌方状态
			if(AbstractStatus.canReachStatus(context, target, StatusEnum.FREEZED)) {
				target.changeStatusWithEndTime(context, StatusEnum.FREEZED, this.getEndTime());
			}
			hasFreezed = true;
		}
	}
}
