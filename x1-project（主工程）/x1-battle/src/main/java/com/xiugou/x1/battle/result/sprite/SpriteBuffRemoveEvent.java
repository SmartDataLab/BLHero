/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import com.xiugou.x1.battle.buff.Buff;
import com.xiugou.x1.battle.result.IActionEvent;

/**
 * @author YY
 *
 */
public class SpriteBuffRemoveEvent implements IActionEvent {

	private int spriteId;
	private Buff buff;

	@Override
	public String buildLog() {
		return String.format("精灵%sBuff变更，移除[%s#%s]", spriteId, buff.getId(), buff.buffEffect().getDesc());
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public Buff getBuff() {
		return buff;
	}

	public void setBuff(Buff buff) {
		this.buff = buff;
	}
	
}
