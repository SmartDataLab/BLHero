package org.gaming.prefab.task;

/**
 * 任务的当前变化
 */
public class TaskChange {
	
	private ITaskType taskType;	
	/**
     * 条件列表
     * @return
     */
	private int[] conditions;
    /**
     * 目标数量，使用long类型是考虑到比如对某怪物造成多少伤害、收入多少金币等
     * @return
     */
	private long targetNum;
	
	public static TaskChange of(ITaskType taskType, int[] conditions, long targetNum) {
		TaskChange change = new TaskChange();
		change.taskType = taskType;
		change.conditions = conditions == null ? new int[0] : conditions;
		change.targetNum = targetNum <= 0 ? 1 : targetNum;
		return change;
	}
	
	public static TaskChange of(ITaskType taskType, long targetNum) {
		return of(taskType, null, targetNum);
	}
	
	public static TaskChange of(ITaskType taskType) {
		return of(taskType, null, 0);
	}
	
	public static TaskChange ofZero(ITaskType taskType) {
		TaskChange change = new TaskChange();
		change.taskType = taskType;
		change.conditions = new int[0];
		change.targetNum = 0;
		return change;
	}

	public ITaskType getTaskType() {
		return taskType;
	}

	public int[] getConditions() {
		return conditions;
	}

	public long getTargetNum() {
		return targetNum;
	}
}
