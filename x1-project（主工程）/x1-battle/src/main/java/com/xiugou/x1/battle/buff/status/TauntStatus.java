/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

import java.util.List;

import com.xiugou.x1.battle.buff.Buff;
import com.xiugou.x1.battle.buff.BuffEffect;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.skill.Skill;

/**
 * @author YY
 *
 */
public class TauntStatus extends AbstractStatus {

	@Override
	public StatusEnum statusTag() {
		return StatusEnum.TAUNT;
	}

	@Override
	protected Skill<?> getStatusSkill(Sprite sprite) {
		return sprite.getNormalSkill();
	}

	@Override
	protected int getStatusTarget(Sprite sprite) {
		List<Buff> buffs = sprite.getBuffs(BuffEffect.TAUNT);
		if(!buffs.isEmpty()) {
			return buffs.get(0).getCaster().getId();
		}
		return 0;
	}
}
