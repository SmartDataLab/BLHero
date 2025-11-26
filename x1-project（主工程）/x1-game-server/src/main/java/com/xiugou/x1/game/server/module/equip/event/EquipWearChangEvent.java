package com.xiugou.x1.game.server.module.equip.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote 装备穿戴变更事件
 */
public class EquipWearChangEvent implements ITaskEvent {
	private long pid;

	public static EquipWearChangEvent of(long pid) {
		EquipWearChangEvent event = new EquipWearChangEvent();
		event.pid = pid;
		return event;
	}

	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.WEAR_EQUIP) };
	}
}
