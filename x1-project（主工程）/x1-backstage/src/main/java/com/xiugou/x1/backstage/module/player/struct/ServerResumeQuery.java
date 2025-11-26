/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class ServerResumeQuery extends PageQuery {
	private int serverUid;
	//开始时间戳
	private int startTime;
	//结束时间戳
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
