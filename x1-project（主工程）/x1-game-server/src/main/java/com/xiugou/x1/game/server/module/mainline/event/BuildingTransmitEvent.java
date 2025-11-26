package com.xiugou.x1.game.server.module.mainline.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 设置传送法阵事件
 */
public class BuildingTransmitEvent implements ITaskEvent {
	private long playerId;
	private int sceneId;
	private int teleportId;

	public static BuildingTransmitEvent of(long playerId, int sceneId, int teleportId) {
		BuildingTransmitEvent event = new BuildingTransmitEvent();
		event.playerId = playerId;
		event.sceneId = sceneId;
		event.teleportId = teleportId;
		return event;
	}

	@Override
	public long getEntityId() {
		return playerId;
	}

	@Override
	public TaskChange[] changes() {
		// TODO 这里是不对的，应该还需要传场景ID，暂时先不改
		return new TaskChange[] { TaskChange.of(TaskType.BUILDING_TRANSMIT, new int[] { teleportId }, 1) };
	}

	public long getPlayerId() {
		return playerId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public int getTeleportId() {
		return teleportId;
	}
}
