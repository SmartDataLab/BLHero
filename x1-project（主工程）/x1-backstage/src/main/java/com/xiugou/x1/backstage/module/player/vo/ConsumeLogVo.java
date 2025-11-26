/**
 * 
 */
package com.xiugou.x1.backstage.module.player.vo;

/**
 * @author YY
 *
 */
public class ConsumeLogVo {
	//序号
	private int index;
	//流水类型编号
	private int gameCause;
	//流水类型名称
	private String gameCauseTxt;
	//资源类型，1金币，2钻石，4木材，5肉，6矿石
	private String resourceType;
	//消费类型，1消耗，2产出
	private int consumeType;
	//消费人数
	private int playerNum;
	//消费次数
	private int countNum;
	//消费总数
	private long total;
	//平均消费数
	private float avg;
	//占比，保留两位小数
	private float weight;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public int getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
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
	public float getAvg() {
		return avg;
	}
	public void setAvg(float avg) {
		this.avg = avg;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public int getGameCause() {
		return gameCause;
	}
	public void setGameCause(int gameCause) {
		this.gameCause = gameCause;
	}
	public String getGameCauseTxt() {
		return gameCauseTxt;
	}
	public void setGameCauseTxt(String gameCauseTxt) {
		this.gameCauseTxt = gameCauseTxt;
	}
}
