/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement.model;

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

/**
 * @author hyy
 *
 */
@Repository
@JvmCache
@Table(name = "achievement", comment = "玩家成就任务表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Achievement extends AbstractEntity implements ITaskContainer {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
	@Column(comment = "成就点数")
	private long point;
	@Column(name = "task_map", comment = "成就任务列表", extra = "text")
	private Map<Integer, Task> taskMap = new HashMap<>();
	@Column(name = "over_tasks", comment = "已经完结的成就", extra = "text")
	private Set<Integer> overTasks = new HashSet<>();
	@Column(name = "point_reward_id", comment = "已经领取的成就点数奖励ID")
	private int pointRewardId;
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public long getPoint() {
		return point;
	}
	public void setPoint(long point) {
		this.point = point;
	}
	public Set<Integer> getOverTasks() {
		return overTasks;
	}
	public void setOverTasks(Set<Integer> overTasks) {
		this.overTasks = overTasks;
	}
	public Map<Integer, Task> getTaskMap() {
		return taskMap;
	}
	public void setTaskMap(Map<Integer, Task> taskMap) {
		this.taskMap = taskMap;
	}
	@Override
	public long getOwnerId() {
		return pid;
	}
	@Override
	public List<Task> getTasks() {
		return new ArrayList<>(taskMap.values());
	}
	public int getPointRewardId() {
		return pointRewardId;
	}
	public void setPointRewardId(int pointRewardId) {
		this.pointRewardId = pointRewardId;
	}
}
