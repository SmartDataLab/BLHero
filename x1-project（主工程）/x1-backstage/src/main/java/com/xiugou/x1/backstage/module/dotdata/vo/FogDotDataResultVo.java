/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class FogDotDataResultVo {
	private int fogId;
	private long minFighting;
	private long num;
	//可解锁人数
	private long canNum;
	private String rate;
	
	public int getFogId() {
		return fogId;
	}
	public void setFogId(int fogId) {
		this.fogId = fogId;
	}
	public long getMinFighting() {
		return minFighting;
	}
	public void setMinFighting(long minFighting) {
		this.minFighting = minFighting;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public long getCanNum() {
		return canNum;
	}
	public void setCanNum(long canNum) {
		this.canNum = canNum;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
}
