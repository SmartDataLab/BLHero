package com.xiugou.x1.game.server.module.home.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/9/1
 * @apiNote
 */
public class MeatChangeEvent implements ITaskEvent {
    private long pid;
    private long number;

    public static MeatChangeEvent of(long pid, long number) {
        MeatChangeEvent event = new MeatChangeEvent();
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
        return new TaskChange[]{TaskChange.of(TaskType.HAVE_MEAT,number)};
    }
}
