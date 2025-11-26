/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.event;

/**
 * @author YY
 *
 */
public class MainlineTaskRewardEvent {
	private long pid;
	private int taskId;
	
	public static MainlineTaskRewardEvent of(long pid, int taskId) {
		MainlineTaskRewardEvent event = new MainlineTaskRewardEvent();
		event.pid = pid;
		event.taskId = taskId;
		return event;
	}

	public long getPid() {
		return pid;
	}

	public int getTaskId() {
		return taskId;
	}
}
