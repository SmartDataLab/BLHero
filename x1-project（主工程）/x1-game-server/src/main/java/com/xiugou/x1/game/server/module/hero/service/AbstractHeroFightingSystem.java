/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.constant.BattleConst;
import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public abstract class AbstractHeroFightingSystem {

	public AbstractHeroFightingSystem() {
		HeroService.register(this);
	}
	
	public abstract FightingScope fightingScope();
	/**
	 * 英雄自身属性
	 * @param hero
	 * @param outAttr
	 */
	public abstract void calculateAttr(Hero hero, BattleAttr outAttr);
	
	/**
	 * 计算团队加成
	 * @param playerId
	 * @param outAttr
	 */
	public abstract void calculateTroopAttr(long playerId, BattleAttr outAttr);

	/**
	 * 结算基础属性
	 * @param outAttr
	 */
	protected void settleBaseAttr(BattleAttr outAttr) {
		long maxHp = (long)(outAttr.getMaxHp() * (1.0f + outAttr.getHpBrate() / BattleConst.WAN));
		outAttr.setMaxHp(maxHp);
		outAttr.setHpBrate(0);
		long atk = (long)(outAttr.getAtk() * (1.0f + outAttr.getAtkBrate() / BattleConst.WAN));
		outAttr.setAtk(atk);
		outAttr.setAtkBrate(0);
	}
}
