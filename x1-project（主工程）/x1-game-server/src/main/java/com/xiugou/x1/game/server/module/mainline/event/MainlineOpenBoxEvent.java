/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
public class MainlineOpenBoxEvent implements ITaskEvent {
	private long playerId;
	private boolean watchAdv;

	public static MainlineOpenBoxEvent of(long playerId, boolean watchAdv) {
		MainlineOpenBoxEvent event = new MainlineOpenBoxEvent();
		event.playerId = playerId;
		event.watchAdv = watchAdv;
		return event;
	}

	@Override
	public long getEntityId() {
		return playerId;
	}

	@Override
	public TaskChange[] changes() {
		if(watchAdv) {
			return new TaskChange[] { TaskChange.of(TaskType.TREASURE_BOX_OPEN), TaskChange.of(TaskType.WATCH_ADV) };
		} else {
			return new TaskChange[] { TaskChange.of(TaskType.TREASURE_BOX_OPEN) };
		}
	}

	public long getPlayerId() {
		return playerId;
	}

	public boolean isWatchAdv() {
		return watchAdv;
	}
}
