package com.xiugou.x1.game.server.module.home.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/8/31
 * @apiNote
 */
public class MineChangeEvent implements ITaskEvent {
    private long pid;
    private long number;

    public static MineChangeEvent of(long pid, long number) {
        MineChangeEvent event = new MineChangeEvent();
        event.pid = pid;
        event.number = number;
        return event;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
        return new TaskChange[]{TaskChange.of(TaskType.HAVE_MINE,number)};
    }
}
