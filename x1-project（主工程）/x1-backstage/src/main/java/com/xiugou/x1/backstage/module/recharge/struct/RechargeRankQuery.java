/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.struct;

import java.util.List;

/**
 * @author YY
 *
 */
public class RechargeRankQuery {
	private int startTime;
	private int endTime;
	private List<Integer> serverUids;
	private int limit;
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
	public List<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(List<Integer> serverUids) {
		this.serverUids = serverUids;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
