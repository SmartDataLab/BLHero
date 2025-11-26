/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

/**
 * @author YY
 *
 */
public class LoginCount {
	private String bornDate;
	private int playerCount;
	private int dayCount;
	public int getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
	public String getBornDate() {
		return bornDate;
	}
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
}
