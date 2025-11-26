/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class LoginDotDataStatisticVo {
	private long channelId;
	private String dateStr;
	private int timing;
	private long num;
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
}
