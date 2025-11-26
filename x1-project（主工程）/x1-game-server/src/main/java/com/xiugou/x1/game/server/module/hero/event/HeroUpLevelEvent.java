package com.xiugou.x1.game.server.module.hero.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 英雄升级事件
 */
public class HeroUpLevelEvent implements ITaskEvent {
	private long pid;

	private int identity;

	public static HeroUpLevelEvent of(long pid, int identity) {
		HeroUpLevelEvent event = new HeroUpLevelEvent();
		event.pid = pid;
		event.identity = identity;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	public int getIdentity() {
		return identity;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.HERO_UP_LEVEL), TaskChange.of(TaskType.HERO_REACH_LEVEL) };
	}
}
