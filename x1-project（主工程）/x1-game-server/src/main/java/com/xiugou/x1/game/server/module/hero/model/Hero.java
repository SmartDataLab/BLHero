/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.sprite.IBattleSprite;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = { "pid", "identity" })
@Table(name = "hero", comment = "英雄信息表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_identity", columns = { "pid", "identity" }, type = IndexType.UNIQUE) })
public class Hero extends AbstractEntity implements IBattleSprite {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(comment = "英雄标识", readonly = true)
	private int identity;
	@Column(comment = "英雄名称", readonly = true)
	private String name;
	@Column(comment = "英雄等级")
	private int level;
	@Column(name = "awaken_level", comment = "觉醒等级")
	private int awakenLevel;
	@Column(comment = "碎片数量")
	private long fragment;
	@Column(name = "his_fragment", comment = "历史获得碎片数量")
	private long hisFragment;
	@Column(comment = "战斗力")
	private long fighting;
	@Column(name = "equip_fighting", comment = "装备提供的战力")
	private long equipFighting;
	@Column(name = "relic_fighting", comment = "遗物提供的战力")
	private long relicFighting;
	@Column(name = "panel_attr", comment = "面板属性", length = 3000)
	private BattleAttr panelAttr = new BattleAttr();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BattleAttr getPanelAttr() {
		return panelAttr;
	}

	public void setPanelAttr(BattleAttr panelAttr) {
		this.panelAttr = panelAttr;
	}

	public long getFighting() {
		return fighting;
	}

	public void setFighting(long fighting) {
		this.fighting = fighting;
	}

	@Override
	public int getConfigId() {
		return 0;
	}

	public long getFragment() {
		return fragment;
	}

	public void setFragment(long fragment) {
		this.fragment = fragment;
	}

	public long getHisFragment() {
		return hisFragment;
	}

	public void setHisFragment(long hisFragment) {
		this.hisFragment = hisFragment;
	}

	public long getEquipFighting() {
		return equipFighting;
	}

	public void setEquipFighting(long equipFighting) {
		this.equipFighting = equipFighting;
	}

	public long getRelicFighting() {
		return relicFighting;
	}

	public void setRelicFighting(long relicFighting) {
		this.relicFighting = relicFighting;
	}

	public int getAwakenLevel() {
		return awakenLevel;
	}

	public void setAwakenLevel(int awakenLevel) {
		this.awakenLevel = awakenLevel;
	}
}
