/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import java.util.List;
import java.util.Set;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.attr.AttrEnumIns;
import com.xiugou.x1.battle.config.ISkillLevelConfig;
import com.xiugou.x1.battle.constant.TargetType;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 * 主动技能
 */
public abstract class ActiveSkill<S> extends Skill<S> {
	//冷却时间
	protected long cdTime;
	
	//首次打击开始时间
	protected long strikeStartTime;
	//打击结束时间
	protected long strikeEndTime;
	//已打击次数
	protected byte strikeNum;
	//技能释放后进行的普攻次数
	protected int normalSkillNum;
	
	public final boolean isCoolDown(Sprite sprite, long now) {
		return checkCoolDown(sprite, now);
	}
	//技能是否已经冷却完成
	protected boolean checkCoolDown(Sprite sprite, long now) {
		if(cdTime < 0) {
			return false;
		}
		boolean coolDown = now > cdTime;
		if(coolDown) {
			strikeNum = 0;
			strikeStartTime = now;
			strikeEndTime = strikeStartTime + skillCfg.getSkillTime();
			if(skillCfg.getCdTime() > 0) {
				cdTime = now + skillCfg.getCdTime();
			} else {
				cdTime = -1;
			}
		}
		return coolDown;
	}
	//是否可以进行技能打击
	public boolean isStrikable(long now) {
		//1、是否在打击时间内
		if(now > strikeEndTime) {
			return false;
		}
		//2、是否有打击次数
		if(skillCfg.getStrikeNum() <= 0) {
			return true;
		}
		return this.strikeNum < skillCfg.getStrikeNum();
	}
	
	public final void selectBaseTargets(BattleContext context, Sprite sprite, List<Integer> targetSpriteIds, List<Sprite> outTargets, Set<Integer> duplicateCheckingSet) {
		int targetNum = 0;
		if(this.skillCfg.getTargetLimit() > 0) {
			targetNum = skillCfg.getTargetLimit();
		} else {
			targetNum = targetSpriteIds.size();
		}
		if(this.skillCfg.getTargetType() == TargetType.SELF.getValue()) {
			outTargets.add(sprite);
		} else if(this.skillCfg.getTargetType() == TargetType.FRIEND.getValue()) {
			for(int i = 0; i < targetSpriteIds.size() && outTargets.size() < targetNum; i++) {
				Integer targetSpriteId = targetSpriteIds.get(i);
				if(duplicateCheckingSet.contains(targetSpriteId)) {
					continue;
				}
				Sprite target = context.getFriends(sprite, targetSpriteId);
				if(target == null) {
					continue;
				}
				if(target.isDeath()) {
					continue;
				}
				target.setInBattle(true);
				outTargets.add(target);
				duplicateCheckingSet.add(targetSpriteId);
			}
		} else if(this.skillCfg.getTargetType() == TargetType.ENEMY.getValue()) {
			for(int i = 0; i < targetSpriteIds.size() && outTargets.size() < targetNum; i++) {
				Integer targetSpriteId = targetSpriteIds.get(i);
				if(duplicateCheckingSet.contains(targetSpriteId)) {
					continue;
				}
				Sprite target = context.getEnemy(sprite, targetSpriteId);
				if(target == null) {
					continue;
				}
				if(target.isDeath()) {
					continue;
				}
				target.setInBattle(true);
				outTargets.add(target);
				duplicateCheckingSet.add(targetSpriteId);
			}
		} else if(this.skillCfg.getTargetType() == TargetType.SPECIAL.getValue()) {
			outTargets.add(sprite);
		} else {
			context.logError("未知的技能{}目标类型{}", this.skillCfg.getSkillId(), this.skillCfg.getTargetType());
		}
	}
	
	public final void doRelease(BattleContext context, Sprite sprite, List<Sprite> targetSprites) {
		releaseBase(context, sprite, targetSprites);
		releaseSpecial(context, sprite, targetSprites);
		strikeNum += 1;
	}
	
	//技能基本逻辑
	public void releaseBase(BattleContext context, Sprite sprite, List<Sprite> targetSprites) {
		if(strikeNum == 0) {
			BuffTriggerManager.beforeRelease(context, sprite, this, targetSprites);
			for(PassiveSkill<?> passiveSkill : sprite.getPassiveSkills()) {
				BuffTriggerManager.beforeRelease(context, sprite, passiveSkill, targetSprites);
			}
		}
		
		for(Sprite target : targetSprites) {
			
			if(this.skillCfg.getTargetType() == TargetType.ENEMY.getValue()) {
				//对敌方的伤害类技能
				SkillDamageResult result = null;
				if(this.skillCfg.getDmgType() == 1) {
					result = this.calculateNormalDamage(context, sprite, target);
				} else {
					result = this.calculateSkillDamage(context, sprite, target);
				}
				target.takeSkillDamage(context, result);
				
				if(result.isHit() && !target.isDeath()) {
					BuffTriggerManager.onHitedTarget(context, sprite, this, target);
					for(PassiveSkill<?> passiveSkill : sprite.getPassiveSkills()) {
						BuffTriggerManager.onHitedTarget(context, sprite, passiveSkill, target);
					}
				}
			} else if(this.skillCfg.getTargetType() == TargetType.SELF.getValue() || this.skillCfg.getTargetType() == TargetType.FRIEND.getValue()) {
				//对己方的治疗类技能
				if(this.getSkillCfg().getSkillFactor() > 0) {
					SkillCureResult result = this.calculateSkillCure(context, sprite, target);
					target.takeSkillCure(context, result);
				}
				BuffTriggerManager.onCuredTarget(context, sprite, this, target);
				for(PassiveSkill<?> passiveSkill : sprite.getPassiveSkills()) {
					BuffTriggerManager.onCuredTarget(context, sprite, passiveSkill, target);
				}
			} else {
				//TODO 特殊的不处理
			}
		}
		
		if(strikeNum == 0) {
			BuffTriggerManager.afterRelease(context, sprite, this);
			for(PassiveSkill<?> passiveSkill : sprite.getPassiveSkills()) {
				BuffTriggerManager.afterRelease(context, sprite, passiveSkill);
			}
		}
	}
	
	public void releaseSpecial(BattleContext context, Sprite sprite, List<Sprite> targetSprites) {
		
	}
	
	//技能冷却完成时
	public void onCdReady(Sprite sprite) {
		buffedTargets.clear();
		
		for(PassiveSkill<?> passiveSkill : sprite.getPassiveSkills()) {
			passiveSkill.buffedTargets.clear();
		}
	}
	//在每个打击帧就绪的时候
	public void onEachHitFrameReady() {

	}
	
	
	/**
	 * 计算普攻伤害
	 * @param context
	 * @param attacker
	 * @param defender
	 * @param now
	 * @return
	 */
	public SkillDamageResult calculateNormalDamage(BattleContext context, Sprite attacker, Sprite defender) {
		long now = context.getNow();
		SkillDamageResult result = new SkillDamageResult();
		result.setAttacker(attacker);
		result.setDefender(defender);
		result.setSkill(this);
		//判断命中
		if(!isHit(context, attacker, defender)) {
			result.setHit(false);
			return result;
		}
		result.setHit(true);
		//最终普攻伤害 =（1*攻方最终攻击*攻方技能伤害比例+攻方技能伤害固定值）*（1+攻方最终伤害加成-防方最终伤害减免）-最终减少固定伤害
		long atk = attacker.getAttrValue(AttrEnumIns.atk, now) * context.getBattleConstCache().getFinalNormalFactor();
		long dmgRate = attacker.getAttrValue(AttrEnumIns.dmgRate, now);
		long reduceDmgRate = defender.getAttrValue(AttrEnumIns.reduceDmgRate, now);
		float damageRate = (10000 + dmgRate - reduceDmgRate) / 10000.0f;
		long reduceDmg = defender.getAttrValue(AttrEnumIns.reduceDmg, now);
		double damage = (atk * this.skillCfg.getSkillFactor() / 10000.0f + this.skillCfg.getFixValue()) * damageRate - reduceDmg;
		
		if(context.getBattleConstCache().getDmgFloat() > 0) {
			long dmgFloat = (long)(damage * (context.getBattleConstCache().getDmgFloat() / 10000.0f));
			int floatResult = context.randomRange(-(int)dmgFloat, (int)dmgFloat);
			damage += floatResult;
		}
		//暴击
		if(isCrit(context, attacker, defender)) {
			result.setCrit(true);
			long critDmgRate = attacker.getAttrValue(AttrEnumIns.critDmgRate, now);
			if(critDmgRate > 0) {
				damage *= critDmgRate / 10000.0f;
			}
		}
		long damage0 = (long)damage;
		if(damage0 <= 0) {
			damage0 = 1;
		}
		result.setDamage(damage0);
		return result;
	}
	
	/**
	 * 计算技能伤害
	 * @param context
	 * @param attacker
	 * @param defender
	 * @param now
	 * @return
	 */
	public SkillDamageResult calculateSkillDamage(BattleContext context, Sprite attacker, Sprite defender) {
		long now = context.getNow();
		SkillDamageResult result = new SkillDamageResult();
		result.setAttacker(attacker);
		result.setDefender(defender);
		result.setSkill(this);
		//判断命中
		if(!isHit(context, attacker, defender)) {
			result.setHit(false);
			return result;
		}
		result.setHit(true);
		//最终技能伤害=(1*攻方最终攻击*攻方技能伤害比例+攻方技能伤害）*技能系数K*（1+攻方最终伤害加成-防方最终伤害减免）-最终减少固定伤害
		long atk = attacker.getAttrValue(AttrEnumIns.atk, now) * context.getBattleConstCache().getFinalSkillFactor().get(0);
		long dmgRate = attacker.getAttrValue(AttrEnumIns.dmgRate, now);
		long reduceDmgRate = defender.getAttrValue(AttrEnumIns.reduceDmgRate, now);
		float damageRate = (10000 + dmgRate - reduceDmgRate) / 10000.0f;
		long reduceDmg = defender.getAttrValue(AttrEnumIns.reduceDmg, now);
		double damage = (atk * this.skillCfg.getSkillFactor() / 10000.0f + this.skillCfg.getFixValue())
				* context.getBattleConstCache().getFinalSkillFactor().get(1) * damageRate - reduceDmg;

		if(context.getBattleConstCache().getDmgFloat() > 0) {
			long dmgFloat = (long)(damage * (context.getBattleConstCache().getDmgFloat() / 10000.0f));
			int floatResult = context.randomRange(-(int)dmgFloat, (int)dmgFloat);
			damage += floatResult;
		}
		//判断暴击
		if(isCrit(context, attacker, defender)) {
			result.setCrit(true);
			//暴击伤害=最终伤害*最终暴伤系数
			long critDmgRate = attacker.getAttrValue(AttrEnumIns.critDmgRate, now);
			if(critDmgRate > 0) {
				damage *= critDmgRate / 10000.0f;
			}
		}
		long damage0 = (long)damage;
		if(damage0 <= 0) {
			damage0 = 1;
		}
		result.setDamage(damage0);
		return result;
	}
	
	public long calculateNormalCure(BattleContext context, Sprite docter, Sprite patient) {
		
		
		return 120;
	}
	
	public SkillCureResult calculateSkillCure(BattleContext context, Sprite docter, Sprite patient) {
		//TODO 计算治疗量
		SkillCureResult result = new SkillCureResult();
		result.setDocter(docter);
		result.setPatient(patient);
		result.setSkill(this);
		result.setCure(10);
		return result;
	}
	
	//是否命中
	public boolean isHit(BattleContext context, Sprite attacker, Sprite defender) {
		//最终真实命中=最终命中-最终闪避
		long hit = attacker.getAttrValue(AttrEnumIns.hit, context.getNow()) - defender.getAttrValue(AttrEnumIns.dodge, context.getNow());
		if(hit < context.getBattleConstCache().getHitRange().get(0)) {
			hit = context.getBattleConstCache().getHitRange().get(0);
		}
		if(hit > context.getBattleConstCache().getHitRange().get(1)) {
			hit = context.getBattleConstCache().getHitRange().get(1);
		}
		return context.randomRate() <= hit;
	}
	
	//是否暴击
	public boolean isCrit(BattleContext context, Sprite attacker, Sprite defender) {
		//最终真实暴击=攻方最终暴击 注：0<=最终真实暴击<=70
		long crit = attacker.getAttrValue(AttrEnumIns.crit, context.getNow());
		if(crit < context.getBattleConstCache().getCritRange().get(0)) {
			crit = context.getBattleConstCache().getCritRange().get(0);
		}
		if(crit > context.getBattleConstCache().getCritRange().get(1)) {
			crit = context.getBattleConstCache().getCritRange().get(1);
		}
		return context.randomRate() <= crit;
	}

	public ISkillLevelConfig getSkillCfg() {
		return skillCfg;
	}

	public void setSkillCfg(ISkillLevelConfig skillCfg) {
		this.skillCfg = skillCfg;
	}
	
	//在攻击命中时触发
	public void onAttackHited(BattleContext context, Sprite attacker, Sprite defender) {}

	public int getNormalSkillNum() {
		return normalSkillNum;
	}
	public void setNormalSkillNum(int normalSkillNum) {
		this.normalSkillNum = normalSkillNum;
	}
	public long getCdTime() {
		return cdTime;
	}
	public void setCdTime(long cdTime) {
		this.cdTime = cdTime;
	}
}
