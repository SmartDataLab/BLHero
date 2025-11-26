/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1009zhigou.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouLimit;

/**
 * @author yy
 *
 */
@Repository
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1009_zhi_gou", comment = "直购活动", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_typeid", columns = { "pid", "type_id" }, type = IndexType.UNIQUE) })
public class ZhiGou extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(name = "limit_buys", comment = "限购信息", length = 5000)
	private Map<Integer, ZhiGouLimit> limitBuys = new HashMap<>();
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "weekly_time", comment = "每周重置时间")
	private LocalDateTime weeklyTime = LocalDateTime.now();
	@Column(name = "monthly_time", comment = "每月重置时间")
	private LocalDateTime monthlyTime = LocalDateTime.now();
	
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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public LocalDateTime getWeeklyTime() {
		return weeklyTime;
	}

	public void setWeeklyTime(LocalDateTime weeklyTime) {
		this.weeklyTime = weeklyTime;
	}

	public LocalDateTime getMonthlyTime() {
		return monthlyTime;
	}

	public void setMonthlyTime(LocalDateTime monthlyTime) {
		this.monthlyTime = monthlyTime;
	}

	public Map<Integer, ZhiGouLimit> getLimitBuys() {
		return limitBuys;
	}

	public void setLimitBuys(Map<Integer, ZhiGouLimit> limitBuys) {
		this.limitBuys = limitBuys;
	}
}
