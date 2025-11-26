/**
 *
 */
package com.xiugou.x1.game.server.module.formation.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.formation.struct.HeroPos;

/**
 * @author yh
 */
@Repository
@JvmCache(relation = { "pid", "type" })
@Table(name = "formation", comment = "上阵信息表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }), @Index(name = "pid_type", columns = { "pid", "type" }, type = IndexType.UNIQUE) })
public class Formation extends AbstractEntity {
	@Id(strategy = Id.Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(comment = "战斗类型", readonly = true)
	private int type;
	@Column(name = "main_hero_identity", comment = "主英雄ID")
	private int mainHeroIdentity;
	@Column(comment = "布阵英雄（英雄标识-位置）", length = 500)
	private Map<Integer, HeroPos> positions = new HashMap<>();
	@Column(comment = "战斗力")
	private long fighting;
	@Column(name = "equip_fighting", comment = "装备提供的战斗力")
	private long equipFighting;
	@Column(name = "manual_change", comment = "是否玩家修改过")
	private boolean manualChange;
	@Column(name = "max_fighting", comment = "历史最高战斗力")
	private long maxFighting;
	
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getFighting() {
		return fighting;
	}

	public void setFighting(long fighting) {
		this.fighting = fighting;
		if(fighting > this.maxFighting) {
			this.maxFighting = fighting;
		}
	}

	public int getMainHeroIdentity() {
		return mainHeroIdentity;
	}

	public void setMainHeroIdentity(int mainHeroIdentity) {
		this.mainHeroIdentity = mainHeroIdentity;
	}

	public long getEquipFighting() {
		return equipFighting;
	}

	public void setEquipFighting(long equipFighting) {
		this.equipFighting = equipFighting;
	}

	public Map<Integer, HeroPos> getPositions() {
		return positions;
	}

	public void setPositions(Map<Integer, HeroPos> positions) {
		this.positions = positions;
	}

	public boolean isManualChange() {
		return manualChange;
	}

	public void setManualChange(boolean manualChange) {
		this.manualChange = manualChange;
	}

	public long getMaxFighting() {
		return maxFighting;
	}
}
