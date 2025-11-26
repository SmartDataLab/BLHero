/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.vo;

/**
 * @author YY
 *
 */
public class TaskDotDataResultVo {
	private int taskId;
	private String taskName;
	private long startNum;
	private long finishNum;
	private float finishRate;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getStartNum() {
		return startNum;
	}
	public void setStartNum(long startNum) {
		this.startNum = startNum;
	}
	public long getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(long finishNum) {
		this.finishNum = finishNum;
	}
	public float getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(float finishRate) {
		this.finishRate = finishRate;
	}
}
