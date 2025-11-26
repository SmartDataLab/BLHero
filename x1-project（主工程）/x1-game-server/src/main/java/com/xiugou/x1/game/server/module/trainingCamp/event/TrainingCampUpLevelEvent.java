package com.xiugou.x1.game.server.module.trainingCamp.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 训练营升级事件
 */
public class TrainingCampUpLevelEvent implements ITaskEvent {

    private long pid;

    public static TrainingCampUpLevelEvent of(long pid) {
        TrainingCampUpLevelEvent event = new TrainingCampUpLevelEvent();
        event.pid = pid;
        return event;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.TRAININGCAMP_UP_LEVEL), TaskChange.of(TaskType.TROOP_NUM) };
    }
}
