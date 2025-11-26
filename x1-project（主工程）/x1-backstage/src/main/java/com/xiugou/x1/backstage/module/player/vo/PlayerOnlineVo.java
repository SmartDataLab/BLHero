/**
 * 
 */
package com.xiugou.x1.backstage.module.player.vo;

/**
 * @author YY
 *
 */
public class PlayerOnlineVo {
	private int period;
	private String periodText;
	private int onlineNum;
	private int newOnlineNum;
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
	public int getNewOnlineNum() {
		return newOnlineNum;
	}
	public void setNewOnlineNum(int newOnlineNum) {
		this.newOnlineNum = newOnlineNum;
	}
	public String getPeriodText() {
		return periodText;
	}
	public void setPeriodText(String periodText) {
		this.periodText = periodText;
	}
}
