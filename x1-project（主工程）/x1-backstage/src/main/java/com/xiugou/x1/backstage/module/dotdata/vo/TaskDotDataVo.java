/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class TaskDotDataVo {
	private int taskId;
	private String taskName;
	private int timing;
	private long num;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
