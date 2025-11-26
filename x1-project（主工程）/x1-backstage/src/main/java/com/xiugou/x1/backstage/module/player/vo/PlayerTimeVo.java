/**
 * 
 */
package com.xiugou.x1.backstage.module.player.vo;

/**
 * @author YY
 *
 */
public class PlayerTimeVo {
	private String timePeriod;
	private int minOnline;
	private int maxOnline;
	private int createNum;
	private int loginNum;
	public int getMinOnline() {
		return minOnline;
	}
	public void setMinOnline(int minOnline) {
		this.minOnline = minOnline;
	}
	public int getMaxOnline() {
		return maxOnline;
	}
	public void setMaxOnline(int maxOnline) {
		this.maxOnline = maxOnline;
	}
	public int getCreateNum() {
		return createNum;
	}
	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}
	public int getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
}
