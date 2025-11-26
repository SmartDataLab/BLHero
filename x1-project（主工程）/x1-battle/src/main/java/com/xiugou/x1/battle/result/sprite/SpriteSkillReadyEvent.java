/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import com.xiugou.x1.battle.result.IActionEvent;

/**
 * @author YY
 *
 */
public class SpriteSkillReadyEvent implements IActionEvent {

	private int spriteId;
	private int skillId;

	@Override
	public String buildLog() {
		return String.format("精灵%s技能%sCD刷新", spriteId, skillId);
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
}
