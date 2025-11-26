/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.model;

import java.time.LocalDateTime;
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
import org.springframework.stereotype.Repository;

/**
 * @author yy
 *
 */
@Repository
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1011_mei_ri_chong_zhi", comment = "每日充值", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_typeid", columns = { "pid", "type_id" }, type = IndexType.UNIQUE) })
public class MeiRiChongZhi extends AbstractEntity {
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
	@Column(name = "recharge_value", comment = "当天充值金额")
	private long rechargeValue;
	@Column(name = "can_reward_id", comment = "可以领取到的奖励ID")
	private int canRewardId;
	@Column(name = "take_rewards", comment = "已经领取过的奖励")
	private List<Integer> takeRewards = new ArrayList<>();
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "reward_settle", comment = "当日奖励是否结算")
	private boolean rewardSettle;
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
	public long getRechargeValue() {
		return rechargeValue;
	}
	public void setRechargeValue(long rechargeValue) {
		this.rechargeValue = rechargeValue;
	}
	public int getCanRewardId() {
		return canRewardId;
	}
	public void setCanRewardId(int canRewardId) {
		this.canRewardId = canRewardId;
	}
	public List<Integer> getTakeRewards() {
		return takeRewards;
	}
	public void setTakeRewards(List<Integer> takeRewards) {
		this.takeRewards = takeRewards;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public boolean isRewardSettle() {
		return rewardSettle;
	}
	public void setRewardSettle(boolean rewardSettle) {
		this.rewardSettle = rewardSettle;
	}
}
