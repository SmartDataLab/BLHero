package com.xiugou.x1.game.server.module.player.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
public class PlayerFightingEvent implements ITaskEvent {
	
	private Player player;

	public static PlayerFightingEvent of(Player player) {
		PlayerFightingEvent event = new PlayerFightingEvent();
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
		return new TaskChange[] { TaskChange.of(TaskType.FIGHTING_NUM, player.getFighting()) };
	}
}
