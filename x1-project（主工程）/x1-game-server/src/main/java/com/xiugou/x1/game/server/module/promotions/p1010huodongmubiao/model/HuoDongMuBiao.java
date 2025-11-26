/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.task.ITaskContainer;
import org.gaming.prefab.task.Task;
import org.springframework.stereotype.Repository;

/**
 * @author yy
 *
 */
@Repository
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1010_huo_dong_mu_biao", comment = "活动目标", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_typeid", columns = { "pid", "type_id" }, type = IndexType.UNIQUE) })
public class HuoDongMuBiao extends AbstractEntity implements ITaskContainer {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(name = "type_name", comment = "活动名字（仅用于看）", readonly = true)
	private String typeName;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(comment = "任务数据", extra = "text")
	private List<Task> tasks = new ArrayList<>();
	@Column(name = "reward_round", comment = "当前奖励轮数")
	private int rewardRound;
	
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
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getRewardRound() {
		return rewardRound;
	}
	public void setRewardRound(int rewardRound) {
		this.rewardRound = rewardRound;
	}
	@Override
	public long getOwnerId() {
		return pid;
	}
}
