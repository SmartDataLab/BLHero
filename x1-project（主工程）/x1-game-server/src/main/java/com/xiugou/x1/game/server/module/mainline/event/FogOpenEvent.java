package com.xiugou.x1.game.server.module.mainline.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 迷雾驱散事件
 */
public class FogOpenEvent implements ITaskEvent {
	private long playerId;
	private int sceneId;
	private int fogId;

	public static FogOpenEvent of(long playerId, int sceneId, int fogId) {
		FogOpenEvent event = new FogOpenEvent();
		event.playerId = playerId;
		event.sceneId = sceneId;
		event.fogId = fogId;
		return event;
	}

	@Override
	public long getEntityId() {
		return playerId;
	}

	@Override
	public TaskChange[] changes() {
		// TODO 这里是不对的，应该还需要传场景ID，暂时先不改
		return new TaskChange[] { TaskChange.of(TaskType.BUILDING_CLEANSING, new int[] { fogId }, 1) };
	}

	public int getSceneId() {
		return sceneId;
	}

	public int getFogId() {
		return fogId;
	}

	public long getPlayerId() {
		return playerId;
	}
}
