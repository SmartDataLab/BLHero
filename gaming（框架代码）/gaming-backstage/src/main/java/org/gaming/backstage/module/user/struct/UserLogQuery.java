/**
 * 
 */
package org.gaming.backstage.module.user.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class UserLogQuery extends PageQuery {
	private String userName;
	//开始时间戳
	private int startTime;
	//结束时间戳
	private int endTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
