/**
 * 
 */
package com.xiugou.x1.game.server.module.player.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
public class PlayerCreateEvent implements ITaskEvent {
	private Player player;
	
	public static PlayerCreateEvent of(Player player) {
		PlayerCreateEvent event = new PlayerCreateEvent();
		event.player = player;
		return event;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public long getEntityId() {
		return player.getId();
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.LOGIN_FIRST_EVERYDAY), TaskChange.of(TaskType.LOGIN) };
	}
}
