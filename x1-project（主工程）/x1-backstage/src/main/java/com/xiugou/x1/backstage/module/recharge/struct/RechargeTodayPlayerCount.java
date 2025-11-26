/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.struct;

import java.time.LocalDateTime;

/**
 * @author YY
 *
 */
public class RechargeTodayPlayerCount {
	private long playerId;
	private long money;
	private LocalDateTime rechargeTime;

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public LocalDateTime getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(LocalDateTime rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
}
