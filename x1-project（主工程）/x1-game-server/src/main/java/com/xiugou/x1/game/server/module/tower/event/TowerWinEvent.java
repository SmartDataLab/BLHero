/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.task.TaskType;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;

/**
 * @author YY
 *
 */
public class TowerWinEvent implements ITaskEvent {
	private long playerId;
	private TowerType towerType;
	private int layer;
	
	public static TowerWinEvent of(long playerId, TowerType towerType, int layer) {
		TowerWinEvent event = new TowerWinEvent();
		event.playerId = playerId;
		event.towerType = towerType;
		event.layer = layer;
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
		return new TaskChange[] {TaskChange.of(TaskType.TOWER_FLOOR, new int[] {towerType.getValue()}, layer)};
	}

	public int getLayer() {
		return layer;
	}

	public TowerType getTowerType() {
		return towerType;
	}
}
