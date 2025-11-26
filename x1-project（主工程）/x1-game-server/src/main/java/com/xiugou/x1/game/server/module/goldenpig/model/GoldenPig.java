/**
 * 
 */
package com.xiugou.x1.game.server.module.goldenpig.model;

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
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "golden_pig", comment = "玩家黄金猪表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class GoldenPig extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "max_stage", comment = "已经通关的层级")
	private int maxStage;
	@Column(name = "challenge_num", comment = "今天已挑战次数")
	private int challengeNum;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "init_ticket", comment = "是否已获取初始化的门票")
	private boolean initTicket;
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getMaxStage() {
		return maxStage;
	}
	public void setMaxStage(int maxStage) {
		this.maxStage = maxStage;
	}
	public int getChallengeNum() {
		return challengeNum;
	}


	public void setChallengeNum(int challengeNum) {
		this.challengeNum = challengeNum;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public boolean isInitTicket() {
		return initTicket;
	}
	public void setInitTicket(boolean initTicket) {
		this.initTicket = initTicket;
	}

}
