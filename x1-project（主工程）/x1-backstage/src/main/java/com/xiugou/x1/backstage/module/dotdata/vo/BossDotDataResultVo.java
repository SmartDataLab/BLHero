/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class BossDotDataResultVo {
	private int bossId;
	private String bossName;
	private int level;
	//可参与人数
	private long canChallengeNum;
	//挑战数量
	private long challengeNum;
	//挑战人数
	private long playerNum;
	private long killNum;
	private long minFighting;
	//击杀率
	private String killRate;
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
	public long getKillNum() {
		return killNum;
	}
	public void setKillNum(long killNum) {
		this.killNum = killNum;
	}
	public long getMinFighting() {
		return minFighting;
	}
	public void setMinFighting(long minFighting) {
		this.minFighting = minFighting;
	}
	public String getKillRate() {
		return killRate;
	}
	public void setKillRate(String killRate) {
		this.killRate = killRate;
	}
	public long getCanChallengeNum() {
		return canChallengeNum;
	}
	public void setCanChallengeNum(long canChallengeNum) {
		this.canChallengeNum = canChallengeNum;
	}
}
