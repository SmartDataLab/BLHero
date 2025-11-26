package com.xiugou.x1.game.server.module.purgatory.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "purgatory", comment = "炼狱轮回", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Purgatory extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(comment = "赛季标识")
	private int round;
	@Column(comment = "当前阶数")
	private int level;
	@Column(comment = "加成", extra = "text")
	private Map<Integer, Attr> attrs = new HashMap<>();
	@Column(name = "free_times", comment = "已使用免费加成次数")
	private int freeTimes;
	@Column(name = "plus_times", comment = "购买的剩余加成次数")
	private int plusTimes;
	@Column(name = "refine_times", comment = "洗练次数")
	private int refineTimes;
	@Column(name = "daily_time", comment = "日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(comment = "炼狱积分")
	private long points;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<Integer, Attr> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<Integer, Attr> attrs) {
		this.attrs = attrs;
	}

	public int getFreeTimes() {
		return freeTimes;
	}

	public void setFreeTimes(int freeTimes) {
		this.freeTimes = freeTimes;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPlusTimes() {
		return plusTimes;
	}

	public void setPlusTimes(int plusTimes) {
		this.plusTimes = plusTimes;
	}

	public int getRefineTimes() {
		return refineTimes;
	}

	public void setRefineTimes(int refineTimes) {
		this.refineTimes = refineTimes;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}
}
