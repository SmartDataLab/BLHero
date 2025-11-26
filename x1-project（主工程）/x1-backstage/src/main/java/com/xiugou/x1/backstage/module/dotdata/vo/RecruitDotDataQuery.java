/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class RecruitDotDataQuery extends PageQuery {
	private int serverUid;
	private int startTime;
	private int endTime;
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
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
}
