/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class BossDotDataVo {
	private int bossId;
	private String bossName;
	private int level;
	private long challengeNum;
	private long playerNum;
	private long minFighting;
	private int timing;
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getChallengeNum() {
		return challengeNum;
	}
	public void setChallengeNum(long challengeNum) {
		this.challengeNum = challengeNum;
	}
	public long getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(long playerNum) {
		this.playerNum = playerNum;
	}
	public long getMinFighting() {
		return minFighting;
	}
	public void setMinFighting(long minFighting) {
		this.minFighting = minFighting;
	}
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
}
