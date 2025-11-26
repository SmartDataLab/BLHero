/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.cross.server.module.arena.struct.ArenaTop;

/**
 * @author hyy
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "arena_season", comment = "竞技场赛季表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ArenaSeason extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "跨服分区ID")
	private int id;
	@Column(comment = "赛季")
	private int season;
	@Column(comment = "上赛季前3", length = 1000)
	private List<ArenaTop> tops = new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeason() {
		return season;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	public List<ArenaTop> getTops() {
		return tops;
	}
	public void setTops(List<ArenaTop> tops) {
		this.tops = tops;
	}
}
