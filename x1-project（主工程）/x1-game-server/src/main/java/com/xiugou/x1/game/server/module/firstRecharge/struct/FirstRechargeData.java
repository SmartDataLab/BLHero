/**
 * 
 */
package com.xiugou.x1.game.server.module.firstRecharge.struct;

/**
 * @author YY
 *
 */
public class FirstRechargeData {
	//充值ID
	private int rechargeId;
	//已经领取的奖励天数
	private int rewardDay;
	//今天可以领取到的奖励天数
	private int canTakeDay;
	public int getRechargeId() {
		return rechargeId;
	}
	public void setRechargeId(int rechargeId) {
		this.rechargeId = rechargeId;
	}
	public int getRewardDay() {
		return rewardDay;
	}
	public void setRewardDay(int rewardDay) {
		this.rewardDay = rewardDay;
	}
	public int getCanTakeDay() {
		return canTakeDay;
	}
	public void setCanTakeDay(int canTakeDay) {
		this.canTakeDay = canTakeDay;
	}
}
