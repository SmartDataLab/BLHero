/**
 * 
 */
package com.xiugou.x1.battle.buff;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.attr.AttrEnum;
import com.xiugou.x1.battle.attr.AttrEnumIns;
import com.xiugou.x1.battle.config.IBuffConfig;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.skill.BuffCureResult;
import com.xiugou.x1.battle.sprite.skill.BuffDamageResult;

/**
 * @author YY
 *
 */
public abstract class Buff {
	protected int id;
	//BUFF的释放者
	protected transient Sprite caster;
	//BUFF的持有者
	protected transient Sprite owner;
	//BUFF结束时间
	protected long endTime;
	//BUFF的详细配置
	protected IBuffConfig buffConfig;
	
	public abstract BuffEffect buffEffect();
	
	public boolean isEnd(long now) {
		return now > endTime;
	}
	
	public long getAttrValue(AttrEnum attrEnum) {
		return 0;
	}
	
	//进入战斗时触发
	public void onEnterBattle(BattleContext context, Sprite sprite) {
		//to be override
	}
	//定时器中触发
	public void onTick(BattleContext context, Sprite sprite) {
		//to be override
	}
	//buff是否需要添加到目标身上
	public boolean needAddToTarget(BattleContext context, Sprite buffOwner) {
		//to be override
		return true;
	}
	/**
	 * 在buff添加到目标身上时触发
	 * @param context
	 * @param caster 施放BUFF的精灵
	 * @param target
	 */
	public void onAddToTarget(BattleContext context, Sprite caster, Sprite buffOwner) {
		//to be override
	}
	
	/**
	 * 在伤害击中敌人时
	 * @param context
	 * @param caster 施放BUFF的精灵
	 * @param enemy
	 */
	public void onHitedEnemy(BattleContext context, Sprite caster, Sprite buffOwner) {
		//to be override
	}
	

	public IBuffConfig getBuffConfig() {
		return buffConfig;
	}

	public void setBuffConfig(IBuffConfig buffConfig) {
		this.buffConfig = buffConfig;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Sprite getCaster() {
		return caster;
	}

	public void setCaster(Sprite caster) {
		this.caster = caster;
	}

	public Sprite getOwner() {
		return owner;
	}

	public void setOwner(Sprite owner) {
		this.owner = owner;
	}
	
	/**
	 * 计算技能伤害
	 * @param context
	 * @param attacker
	 * @param defender
	 * @param now
	 * @return
	 */
	public BuffDamageResult calculateBuffDamage(BattleContext context, Sprite owner) {
		long now = context.getNow();
		BuffDamageResult result = new BuffDamageResult();
		result.setAttacker(caster);
		result.setDefender(owner);
		result.setBuff(this);
		
		//最终技能伤害=(1*攻方最终攻击*攻方技能伤害比例+攻方技能伤害）*技能系数K*（1+攻方最终伤害加成-防方最终伤害减免）-最终减少固定伤害
		long atk = caster.getAttrValue(AttrEnumIns.atk, now) * context.getBattleConstCache().getFinalSkillFactor().get(0);
		long dmgRate = caster.getAttrValue(AttrEnumIns.dmgRate, now);
		long reduceDmgRate = caster.getAttrValue(AttrEnumIns.reduceDmgRate, now);
		float damageRate = (10000 + dmgRate - reduceDmgRate) / 10000.0f;
		long reduceDmg = caster.getAttrValue(AttrEnumIns.reduceDmg, now);
		double damage = (atk * this.getSkillFactor() / 10000.0f + this.getSkillFixValue())
				* context.getBattleConstCache().getFinalSkillFactor().get(1) * damageRate - reduceDmg;
		
		if(context.getBattleConstCache().getDmgFloat() > 0) {
			long dmgFloat = (long)(damage * (context.getBattleConstCache().getDmgFloat() / 10000.0f));
			int floatResult = context.randomRange(-(int)dmgFloat, (int)dmgFloat);
			damage += floatResult;
		}
		long damage0 = (long)damage;
		if(damage0 <= 0) {
			damage0 = 1;
		}
		result.setDamage(damage0);
		return result;
	}
	
	public BuffCureResult calculateBuffCure(BattleContext context, Sprite owner) {
		long cure = 12;
		
		BuffCureResult result = new BuffCureResult();
		result.setDocter(this.caster);
		result.setPatient(owner);
		result.setCure(cure);
		return result;
	}
	
	//技能系数
	protected int getSkillFactor() {
		//to be override
		return 0;
	}
	//技能固定系数
	protected int getSkillFixValue() {
		//to be override
		return 0;
	}
}
