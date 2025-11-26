/**
 * 
 */
package org.gaming.prefab.task;

/**
 * @author YY
 * 任务检测包装
 */
public class TaskWrapper {
	private Task task;
	private ITaskConfig taskConfig;
	
	public static TaskWrapper of(Task task, ITaskConfig taskConfig) {
		TaskWrapper wrapper = new TaskWrapper();
		wrapper.task = task;
		wrapper.taskConfig = taskConfig;
		return wrapper;
	}
	public Task getTask() {
		return task;
	}
	public ITaskConfig getTaskConfig() {
		return taskConfig;
	}
}
