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
public class MainlineCampAdvEvent implements ITaskEvent {
	private long playerId;

	public static MainlineCampAdvEvent of(long playerId) {
		MainlineCampAdvEvent event = new MainlineCampAdvEvent();
		event.playerId = playerId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	@Override
	public long getEntityId() {
		return playerId;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.WATCH_ADV) };
	}
}
