package com.xiugou.x1.game.server.module.villagedefense.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author yh
 * @date 2023/8/16
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "village", comment = "村庄守卫", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Village extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(comment = "最大关卡")
	private int level;
	@Column(name = "challenge_times", comment = "今日挑战次数")
	private int challengeTimes;
	@Column(comment = "赛季标识")
	private int round;
	@Column(comment = "积分")
	private long point;
	@Column(name = "daily_time", comment = "日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();

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

	public int getChallengeTimes() {
		return challengeTimes;
	}

	public long getPoint() {
		return point;
	}

	public void setPoint(long point) {
		this.point = point;
	}

	public void setChallengeTimes(int challengeTimes) {
		this.challengeTimes = challengeTimes;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
}
