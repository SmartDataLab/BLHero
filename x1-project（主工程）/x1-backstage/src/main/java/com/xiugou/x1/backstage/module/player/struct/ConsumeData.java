/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

/**
 * @author YY
 *
 */
public class ConsumeData {
	private int gameCause;
	private int playerNum;
	private int countNum;
	private long total;
	public int getGameCause() {
		return gameCause;
	}
	public void setGameCause(int gameCause) {
		this.gameCause = gameCause;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
