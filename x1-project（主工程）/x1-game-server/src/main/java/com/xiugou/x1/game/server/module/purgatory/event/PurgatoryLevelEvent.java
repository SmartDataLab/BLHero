package com.xiugou.x1.game.server.module.purgatory.event;


import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/8/10
 * @apiNote 炼狱轮回通关事件
 */
public class PurgatoryLevelEvent implements ITaskEvent {
    private long pid;

    private int level;

    public static PurgatoryLevelEvent of(long pid, int level) {
        PurgatoryLevelEvent event = new PurgatoryLevelEvent();
        event.pid = pid;
        event.level = level;
        return event;
    }

    public long getPid() {
        return pid;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
        return new TaskChange[]{TaskChange.of(TaskType.COMPLETE_DUPLICATE, new int[]{BattleType.PURGATORY.getValue()}, 1)};

    }
}
