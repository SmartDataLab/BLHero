/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import com.xiugou.x1.battle.result.IActionEvent;

/**
 * @author YY
 *
 */
public class SpriteCureEvent implements IActionEvent {
	private int spriteId;
	private long hp;
	private long cure;
	
	@Override
	public String buildLog() {
		return String.format("精灵%s被治疗，剩余血量%s，治疗量%s", spriteId, hp, cure);
	}
	public int getSpriteId() {
		return spriteId;
	}
	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}
	public long getHp() {
		return hp;
	}
	public void setHp(long hp) {
		this.hp = hp;
	}
	public long getCure() {
		return cure;
	}
	public void setCure(long cure) {
		this.cure = cure;
	}
}
