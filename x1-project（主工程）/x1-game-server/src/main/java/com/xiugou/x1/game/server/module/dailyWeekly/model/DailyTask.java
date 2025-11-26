/**
 * 
 */
package com.xiugou.x1.game.server.module.dailyWeekly.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.task.ITaskContainer;
import org.gaming.prefab.task.Task;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "daily_task", comment = "日常任务表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class DailyTask extends AbstractEntity implements DailyResetEntity, ITaskContainer {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "day_points", comment = "日常积分")
	private long dayPoints;
	@Column(name = "day_point_reward", comment = "已领日常活跃奖励")
	private List<Integer> dayPointReward = new ArrayList<>();
	@Column(name = "daily_tasks", comment = "日常任务列表", extra = "text")
	private List<Task> dailyTasks = new ArrayList<>();
	@Column(name = "daily_time", comment = "日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getDayPoints() {
		return dayPoints;
	}

	public void setDayPoints(long dayPoints) {
		this.dayPoints = dayPoints;
	}

	public List<Integer> getDayPointReward() {
		return dayPointReward;
	}

	public void setDayPointReward(List<Integer> dayPointReward) {
		this.dayPointReward = dayPointReward;
	}

	public List<Task> getDailyTasks() {
		return dailyTasks;
	}

	public void setDailyTasks(List<Task> dailyTasks) {
		this.dailyTasks = dailyTasks;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	@Override
	public long getOwnerId() {
		return pid;
	}

	@Override
	public List<Task> getTasks() {
		return dailyTasks;
	}
}
