/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.battle.attr.BattleAttr;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "hero_troop", comment = "玩家英雄团队数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class HeroTroop extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "troop_attr", comment = "团队面板属性", length = 2500)
	private BattleAttr troopAttr = new BattleAttr();
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public BattleAttr getTroopAttr() {
		return troopAttr;
	}
	public void setTroopAttr(BattleAttr troopAttr) {
		this.troopAttr = troopAttr;
	}
}
