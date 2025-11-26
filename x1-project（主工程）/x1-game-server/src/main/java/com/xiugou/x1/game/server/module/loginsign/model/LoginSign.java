/**
 * 
 */
package com.xiugou.x1.game.server.module.loginsign.model;

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
@Table(name = "login_sign", comment = "玩家登录和签到表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class LoginSign extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "login_day", comment = "已经领取完的登录奖励天数")
	private int loginDay;
	@Column(name = "sign_day", comment = "已经领取完的登录奖励天数")
	private int signDay;
	@Column(name = "take_sign_reward", comment = "是否可以领取登录奖励")
	private boolean takeSignReward;
	@Column(name = "take_login_reward", comment = "是否可以领取登录奖励")
	private boolean takeLoginReward;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public int getLoginDay() {
		return loginDay;
	}
	public void setLoginDay(int loginDay) {
		this.loginDay = loginDay;
	}
	public int getSignDay() {
		return signDay;
	}
	public void setSignDay(int signDay) {
		this.signDay = signDay;
	}
	public boolean isTakeLoginReward() {
		return takeLoginReward;
	}
	public void setTakeLoginReward(boolean takeLoginReward) {
		this.takeLoginReward = takeLoginReward;
	}
	public boolean isTakeSignReward() {
		return takeSignReward;
	}
	public void setTakeSignReward(boolean takeSignReward) {
		this.takeSignReward = takeSignReward;
	}
}
