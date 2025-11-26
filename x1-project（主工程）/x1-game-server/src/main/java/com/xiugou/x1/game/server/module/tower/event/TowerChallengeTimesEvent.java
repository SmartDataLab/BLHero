package com.xiugou.x1.game.server.module.tower.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 特定塔挑战次数事件
 */
public class TowerChallengeTimesEvent implements ITaskEvent {
    private long pid;
    private int type;

    public static TowerChallengeTimesEvent of(long pid, int type) {
        TowerChallengeTimesEvent event = new TowerChallengeTimesEvent();
        event.pid = pid;
        event.type = type;
        return event;
    }


    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
        return new TaskChange[]{TaskChange.of(TaskType.TOWER_CHALLENGE_TIMES, new int[]{type}, 1),
                TaskChange.of(TaskType.TOWER_CHALLENGE_NUM)};
    }
}
