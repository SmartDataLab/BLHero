package com.xiugou.x1.game.server.module.mainline.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 与特定NPC交互事件
 */
public class NpcInteractionEvent implements ITaskEvent {
	private long pid;
	private int npcId;

	public static NpcInteractionEvent of(long pid, int npcId) {
		NpcInteractionEvent event = new NpcInteractionEvent();
		event.pid = pid;
		event.npcId = npcId;
		return event;
	}


	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[]{TaskChange.of(TaskType.NPC_INTERACTION, new int[]{npcId}, 1)};
	}
}
