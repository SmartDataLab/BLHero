/**
 * 
 */
package com.xiugou.x1.game.server.module.task;

import org.gaming.prefab.task.ITaskSystem;

/**
 * @author YY
 *
 */
public enum TaskSystem implements ITaskSystem {
	
	MAINLINE(1, "主线任务系统"),
	DAILY_TASK(2, "每日任务系统"),
	WEEKLY_TASK(3, "每周任务系统"),
	QRMB_TASK(4, "七日目标任务系统"),
	HDMB_TASK(5, "活动目标任务系统"),
	ZHANLING_TASK(6, "战令目标任务系统"),
	ACHIEVEMENT(7, "成就任务系统"),
	;

	private final int value;
	private final String desc;
	private TaskSystem(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public String getDesc() {
		return desc;
	}
}
