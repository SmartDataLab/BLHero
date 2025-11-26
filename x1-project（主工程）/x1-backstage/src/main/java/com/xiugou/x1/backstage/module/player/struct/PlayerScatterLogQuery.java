/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PlayerScatterLogQuery {
	private int startTime;
	private int endTime;
	private List<Integer> serverUids = new ArrayList<>();
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
}
