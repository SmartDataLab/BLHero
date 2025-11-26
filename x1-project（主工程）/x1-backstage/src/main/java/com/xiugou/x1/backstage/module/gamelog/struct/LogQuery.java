/**
 * 
 */
package com.xiugou.x1.backstage.module.gamelog.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class LogQuery extends PageQuery {
	//服务器ID
	private int serverUid;
	//开始时间戳
	private int startTime;
	//结束时间戳
	private int endTime;
	//玩家ID
	private long playerId;
	//流水事件
	private int cause;
	
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
	public int getCause() {
		return cause;
	}
	public void setCause(int cause) {
		this.cause = cause;
	}
}
