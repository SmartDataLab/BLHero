package com.xiugou.x1.game.server.module.home.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 解锁特建筑事件
 */
public class UnlockBuildingsEvent implements ITaskEvent {
	private long pid;
	private int buildingId;

	public static UnlockBuildingsEvent of(long pid, int buildingId) {
		UnlockBuildingsEvent event = new UnlockBuildingsEvent();
		event.pid = pid;
		event.buildingId = buildingId;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[]{TaskChange.of(TaskType.UNLOCK_BUILDINGS, new int[]{buildingId}, 1)};
	}
}
