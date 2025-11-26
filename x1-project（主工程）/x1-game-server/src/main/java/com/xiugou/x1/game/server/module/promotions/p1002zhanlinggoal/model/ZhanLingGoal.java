/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
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
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1002_zhan_ling_goal", comment = "经验类战令", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }) })
public class ZhanLingGoal extends AbstractEntity implements ITaskContainer {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(name = "type_name", comment = "活动类型名字", readonly = true)
	private String typeName;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(comment = "目标任务数据", extra = "text")
	private List<Task> tasks = new ArrayList<>();
	@Column(name = "premium_tasks", comment = "已经领取高级奖励的任务ID", length = 2000)
	private Set<Integer> premiumTasks = new HashSet<>();
	@Column(name = "buy_premium", comment = "是否已经购买高级通行证")
	private boolean buyPremium;

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

	public boolean isBuyPremium() {
		return buyPremium;
	}

	public void setBuyPremium(boolean buyPremium) {
		this.buyPremium = buyPremium;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public Set<Integer> getPremiumTasks() {
		return premiumTasks;
	}

	public void setPremiumTasks(Set<Integer> premiumTasks) {
		this.premiumTasks = premiumTasks;
	}

	@Override
	public long getOwnerId() {
		return pid;
	}
}
