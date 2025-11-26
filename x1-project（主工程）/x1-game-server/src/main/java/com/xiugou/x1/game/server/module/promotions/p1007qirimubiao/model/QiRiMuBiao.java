/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
@Table(name = "p1007_qi_ri_mu_biao", comment = "七日目标", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class QiRiMuBiao extends AbstractEntity implements DailyResetEntity, ITaskContainer {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(comment = "玩家是否已经达到开启该活动的条件")
	private boolean opened;
	@Column(comment = "当前所处天数")
	private int day;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "task_map", comment = "任务数据", extra = "text")
	private Map<Integer, Task> taskMap = new HashMap<>();
	@Column(name = "end_time", comment = "活动结束时间")
	private LocalDateTime endTime = LocalDateTime.now();
	@Column(name = "stage_rewards", comment = "已经领取的阶段奖励")
	private Set<Integer> stageRewards = new HashSet<>();
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public Set<Integer> getStageRewards() {
		return stageRewards;
	}
	public void setStageRewards(Set<Integer> stageRewards) {
		this.stageRewards = stageRewards;
	}
	@Override
	public long getOwnerId() {
		return pid;
	}
	@Override
	public List<Task> getTasks() {
		return new ArrayList<>(taskMap.values());
	}
	public Map<Integer, Task> getTaskMap() {
		return taskMap;
	}
	public void setTaskMap(Map<Integer, Task> taskMap) {
		this.taskMap = taskMap;
	}
}
