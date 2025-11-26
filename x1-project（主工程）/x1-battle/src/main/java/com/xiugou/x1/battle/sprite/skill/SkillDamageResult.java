/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.result.IResult;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class SkillDamageResult implements IResult, IDamageResult {
	private Sprite attacker;
	private Sprite defender;
	private ActiveSkill<?> skill;
	private boolean hit;
	private boolean crit;
	private long damage;
	
	public boolean isHit() {
		return hit;
	}
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	public boolean isCrit() {
		return crit;
	}
	public void setCrit(boolean crit) {
		this.crit = crit;
	}
	public long getDamage() {
		return damage;
	}
	public void setDamage(long damage) {
		this.damage = damage;
	}
	public Sprite getAttacker() {
		return attacker;
	}
	public void setAttacker(Sprite attacker) {
		this.attacker = attacker;
	}
	public Sprite getDefender() {
		return defender;
	}
	public void setDefender(Sprite defender) {
		this.defender = defender;
	}
	@Override
	public String toString() {
		return String.format("技能%s伤害%s，命中%s，暴击%s", skill.getSkillId(), damage, hit, crit);
	}
	public ActiveSkill<?> getSkill() {
		return skill;
	}
	public void setSkill(ActiveSkill<?> skill) {
		this.skill = skill;
	}
}
