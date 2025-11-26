/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import com.xiugou.x1.battle.result.IActionEvent;
import com.xiugou.x1.battle.sprite.skill.IDamageResult;

/**
 * @author YY
 *
 */
public class SpriteInjureEvent implements IActionEvent {
	private int spriteId;
	private long hp;
	private IDamageResult damageResult;
	
	@Override
	public String buildLog() {
		return String.format("精灵%s承受伤害，剩余血量%s，受到%s", spriteId, hp, damageResult);
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
	public IDamageResult getDamageResult() {
		return damageResult;
	}
	public void setDamageResult(IDamageResult damageResult) {
		this.damageResult = damageResult;
	}
}
