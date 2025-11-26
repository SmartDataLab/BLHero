/**
 * 
 */
package com.xiugou.x1.game.server.module.goldenpig.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
public class ChallengeGoldenPigEvent implements ITaskEvent {
	
	private long playerId;
	
	public static ChallengeGoldenPigEvent of(long playerId) {
		ChallengeGoldenPigEvent event = new ChallengeGoldenPigEvent();
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
		return new TaskChange[] {
				TaskChange.of(TaskType.COMPLETE_DUPLICATE, new int[] { BattleType.GOLDEN_PIG.getValue() }, 1) };
	}
	
}
