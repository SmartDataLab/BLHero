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
public class PlayerLoginEvent implements ITaskEvent {
	private Player player;

	private boolean firstLogin; // 是否今天首登

	public static PlayerLoginEvent of(Player player, boolean firstLogin) {
		PlayerLoginEvent event = new PlayerLoginEvent();
		event.player = player;
		event.firstLogin = firstLogin;
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
		if (firstLogin) {
			return new TaskChange[] {TaskChange.of(TaskType.LOGIN_FIRST_EVERYDAY)};
		} else {
			return new TaskChange[] { TaskChange.of(TaskType.LOGIN) };
		}
	}
}
