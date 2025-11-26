package com.xiugou.x1.game.server.module.dungeon.event;

import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/10/26
 * @apiNote 挑战指定地下城副本事件
 */
public class ChallengeDungeonsEvent implements ITaskEvent {
    private long pid;
    private int dungeonId;

    public static ChallengeDungeonsEvent of(long pid, int dungeonId) {
        ChallengeDungeonsEvent event = new ChallengeDungeonsEvent();
        event.pid = pid;
        event.dungeonId = dungeonId;
        return event;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.CHALLENGE_DUNGEONS, new int[] { dungeonId }, 1),
				TaskChange.of(TaskType.COMPLETE_DUPLICATE, new int[] { BattleType.DUNGEON.getValue() }, 1) };
    }
}
