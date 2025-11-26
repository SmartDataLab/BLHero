package com.xiugou.x1.game.server.module.mainline.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 营地开启事件
 */
public class CampSetupEvent implements ITaskEvent {
	private long pid;

	public static CampSetupEvent of(long pid) {
		CampSetupEvent event = new CampSetupEvent();
		event.pid = pid;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] {TaskChange.of(TaskType.CAMP_SETUP)};
	}
}
