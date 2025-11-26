package com.xiugou.x1.game.server.module.mainline.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 营地开启事件事件
 */
public class CampTimeEvent implements ITaskEvent {
	private long pid;
	private long time;

	public static CampTimeEvent of(long pid, long time) {
		CampTimeEvent event = new CampTimeEvent();
		event.pid = pid;
		event.time = time;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		if(time <= 0) {
			return new TaskChange[0];
		}
		return new TaskChange[] { TaskChange.of(TaskType.CAMP_TIME, time) };
	}

	public long getPid() {
		return pid;
	}

	public long getTime() {
		return time;
	}
}
