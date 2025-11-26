package org.gaming.prefab.task;

/**
 * 任务状态
 */
public enum TaskStatus {

    /** 未完成 */
    UNDO(0),

    /** 已完成 */
    DONE(1),

    /** 已领取 */
    EMPTY(2),
    
    ;

	private int value;
	private TaskStatus(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
}
