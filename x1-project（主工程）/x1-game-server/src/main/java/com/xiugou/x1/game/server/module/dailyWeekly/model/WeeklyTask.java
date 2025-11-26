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

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.WeeklyResetEntity;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "weekly_task", comment = "周常任务表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class WeeklyTask extends AbstractEntity implements WeeklyResetEntity, ITaskContainer {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "week_points", comment = "周常积分")
    private long weekPoints;
    @Column(name = "week_point_reward", comment = "已领周常活跃奖励")
    private List<Integer> weekPointReward = new ArrayList<>();
    @Column(name = "weekly_tasks", comment = "周常任务列表", extra = "text")
    private List<Task> weeklyTasks = new ArrayList<>();
    @Column(name = "weekly_time", comment = "周重置时间")
    private LocalDateTime weeklyTime = LocalDateTime.now();
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public long getWeekPoints() {
		return weekPoints;
	}
	public void setWeekPoints(long weekPoints) {
		this.weekPoints = weekPoints;
	}
	public List<Integer> getWeekPointReward() {
		return weekPointReward;
	}
	public void setWeekPointReward(List<Integer> weekPointReward) {
		this.weekPointReward = weekPointReward;
	}
	public List<Task> getWeeklyTasks() {
		return weeklyTasks;
	}
	public void setWeeklyTasks(List<Task> weeklyTasks) {
		this.weeklyTasks = weeklyTasks;
	}
	public LocalDateTime getWeeklyTime() {
		return weeklyTime;
	}
	public void setWeeklyTime(LocalDateTime weeklyTime) {
		this.weeklyTime = weeklyTime;
	}
	@Override
	public long getOwnerId() {
		return pid;
	}
	@Override
	public List<Task> getTasks() {
		return weeklyTasks;
	}
}
