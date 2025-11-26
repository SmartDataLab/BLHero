package org.gaming.prefab.task;

/**
 * 任务进度
 */
public class Task {
	/**
	 * 任务ID
	 */
	private int id;
	/**
	 * 当前进度
	 */
	private long process;
	/**
	 * 任务状态
	 */
	private int status;

	public static Task create(int id) {
		Task task = new Task();
		task.id = id;
		task.process = 0;
		task.status = 0;
		return task;
	}

	public boolean isDone() {
		return status == TaskStatus.DONE.getValue();
	}
	public boolean isEmpty() {
		return status == TaskStatus.EMPTY.getValue();
	}
	/**
	 * 
	 * @param goalNum 完成任务的目标数量
	 */
	public void checkFinish(long goalNum) {
		if(status == TaskStatus.UNDO.getValue() && process >= goalNum) {
			status = TaskStatus.DONE.getValue();
		}
	}
	
	/**
	 * 标记任务已完成
	 */
	public void markDone() {
		status = TaskStatus.DONE.getValue();
	}
	
	/**
	 * 标记任务已领取
	 */
	public void markReceived() {
		status = TaskStatus.EMPTY.getValue();
	}
	
	public int getId() {
		return id;
	}

	public long getProcess() {
		return process;
	}

	public void setProcess(long process) {
		this.process = process;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
