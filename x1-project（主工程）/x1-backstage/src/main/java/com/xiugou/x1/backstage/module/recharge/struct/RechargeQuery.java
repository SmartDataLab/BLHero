/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class RechargeQuery extends PageQuery {
	private long playerId;
	private String nick;
	private String openId;
	private String sdkOrderId;
	private int startTime;
	private int endTime;
	//订单状态   -1全部，0未处理，1发货成功，2发货失败
	private int status = -1;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getSdkOrderId() {
		return sdkOrderId;
	}
	public void setSdkOrderId(String sdkOrderId) {
		this.sdkOrderId = sdkOrderId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
