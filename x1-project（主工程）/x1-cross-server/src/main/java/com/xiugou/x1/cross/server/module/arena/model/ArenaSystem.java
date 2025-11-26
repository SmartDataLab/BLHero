/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "arena_system", comment = "竞技场系统表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ArenaSystem extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "跨服分区ID")
	private long id;
	@Column(name = "settle_time", comment = "赛季结算时间", extra = "time")
	private long settleTime;
	@Column(name = "daily_time", comment = "每日结算时间", extra = "time")
	private long dailyTime;
	
	public long getSettleTime() {
		return settleTime;
	}
	public void setSettleTime(long settleTime) {
		this.settleTime = settleTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(long dailyTime) {
		this.dailyTime = dailyTime;
	}
}
