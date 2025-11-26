package com.xiugou.x1.game.server.module.dungeon.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
 * @date 2023/7/10
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "dungeon", comment = "地下城表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Dungeon extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "normal_dungeons",comment = "日常副本通已关ID")
	private Set<Integer> normalDungeons = new HashSet<>();
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public Set<Integer> getNormalDungeons() {
		return normalDungeons;
	}

	public void setNormalDungeons(Set<Integer> normalDungeons) {
		this.normalDungeons = normalDungeons;
	}

	@Override
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	@Override
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
}
