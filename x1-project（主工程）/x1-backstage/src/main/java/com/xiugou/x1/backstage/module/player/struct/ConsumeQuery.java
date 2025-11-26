/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class ConsumeQuery extends PageQuery {
	private int serverUid;
	//开始时间戳
	private int startTime;
	//结束时间戳
	private int endTime;
	//资源类型，
	private int resourceType;
	//1消费，2产出
	private int consumeType;
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
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
	}
}
