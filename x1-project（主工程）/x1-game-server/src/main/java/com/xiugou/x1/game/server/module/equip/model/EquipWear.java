package com.xiugou.x1.game.server.module.equip.model;

import org.gaming.db.annotation.*;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "equip_wear", comment = "装备穿戴表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class EquipWear extends AbstractEntity {
	@Id(strategy = Id.Strategy.IDENTITY)
	@Column(name = "pid", comment = "玩家ID")
	private long pid;
	@Column(comment = "穿戴列表（部位：装备唯一ID）", length = 500)
	private Map<Integer, Long> wearing = new HashMap<>();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public Map<Integer, Long> getWearing() {
		return wearing;
	}

	public void setWearing(Map<Integer, Long> wearing) {
		this.wearing = wearing;
	}
}
