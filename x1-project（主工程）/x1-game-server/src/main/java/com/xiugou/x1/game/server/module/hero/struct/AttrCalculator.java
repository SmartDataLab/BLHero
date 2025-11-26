/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.struct;

import com.xiugou.x1.battle.attr.BattleAttr;

/**
 * @author YY
 *
 */
public class AttrCalculator {
	private long oldFighting;
	private long newFighting;
	private BattleAttr battleAttr;
	public long getOldFighting() {
		return oldFighting;
	}
	public void setOldFighting(long oldFighting) {
		this.oldFighting = oldFighting;
	}
	public long getNewFighting() {
		return newFighting;
	}
	public void setNewFighting(long newFighting) {
		this.newFighting = newFighting;
	}
	public BattleAttr getBattleAttr() {
		return battleAttr;
	}
	public void setBattleAttr(BattleAttr battleAttr) {
		this.battleAttr = battleAttr;
	}
	
}
