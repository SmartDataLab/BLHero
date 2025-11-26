/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.buff.Buff;
import com.xiugou.x1.battle.result.IResult;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class BuffDamageResult implements IResult, IDamageResult {
	private Sprite attacker;
	private Sprite defender;
	private long damage;
	private Buff buff;
	
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
	public boolean isCrit() {
		return false;
	}
	@Override
	public boolean isHit() {
		return true;
	}
	@Override
	public String toString() {
		return String.format("Buff[%s]伤害%s", buff.buffEffect().getDesc(), damage);
	}
	public Buff getBuff() {
		return buff;
	}
	public void setBuff(Buff buff) {
		this.buff = buff;
	}
}
