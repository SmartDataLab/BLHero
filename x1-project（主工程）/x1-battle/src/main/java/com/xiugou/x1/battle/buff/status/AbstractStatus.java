/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.skill.Skill;

/**
 * @author YY
 * 凡事能影响精灵行为的都称为状态，状态不等于BUFF
 */
public abstract class AbstractStatus {
	//状态结束时间
	protected long time;
	
	public abstract StatusEnum statusTag();
	
	public final boolean canReachStatus(StatusEnum status) {
		return statusTag().canReachStatus(status);
	}
	
	/**
	 * 该状态下是否可以做出动作
	 * @return
	 */
	protected boolean canAction() {
		
		return true;
	}
	/**
	 * 是否免疫伤害
	 * @return
	 */
	protected boolean immuneDamage() {
		return false;
	}
	/**
	 * 状态下的技能
	 * @param sprite
	 * @return
	 */
	protected Skill<?> getStatusSkill(Sprite sprite) {
		return null;
	}
	/**
	 * 状态下的目标
	 * @param sprite
	 * @return
	 */
	protected int getStatusTarget(Sprite sprite) {
		return 0;
	}
	
	public final long getTime() {
		return time;
	}

	public final void setTime(long time) {
		this.time = time;
	}
	
	public final boolean isEnd(BattleContext context) {
		return context.getNow() >= time;
	}
	
	/**
	 * 是否可以兼容新的状态
	 * @param sprite
	 * @param statusEnum
	 * @return
	 */
	public static boolean canReachStatus(BattleContext context, Sprite sprite, StatusEnum statusEnum) {
		boolean canReach = true;
		for(AbstractStatus status : sprite.getStatus()) {
			if(!status.isEnd(context)) {
				canReach = canReach && status.canReachStatus(statusEnum);
			}
		}
		return canReach;
	}

	/**
	 * 是否免疫伤害
	 * @param sprite
	 * @return
	 */
	public static boolean immuneDamage(BattleContext context, Sprite sprite) {
		boolean immuneDamage = false;
		for(AbstractStatus status : sprite.getStatus()) {
			if(!status.isEnd(context)) {
				immuneDamage = immuneDamage || status.immuneDamage();
			}
		}
		return immuneDamage;
	}
	
	/**
	 * 获取改变普攻攻击行为的状态
	 * @param sprite
	 * @return
	 */
	public static AbstractStatus getSkillStatus(BattleContext context, Sprite sprite) {
		AbstractStatus atkStatus = null;
		for(AbstractStatus status : sprite.getStatus()) {
			if(!status.isEnd(context) && status.getStatusSkill(sprite) != null) {
				atkStatus = status;
				break;
			}
		}
		return atkStatus;
	}
	
	public static boolean canAction(BattleContext context, Sprite sprite) {
		boolean canAction = true;
		for(AbstractStatus status : sprite.getStatus()) {
			if(!status.isEnd(context)) {
				canAction = canAction && status.canAction();
			}
		}
		return canAction;
	}
}
