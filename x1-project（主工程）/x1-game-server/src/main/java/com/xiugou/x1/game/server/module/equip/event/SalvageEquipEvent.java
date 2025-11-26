package com.xiugou.x1.game.server.module.equip.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/8/15
 * @apiNote 装备分解事件
 */
public class SalvageEquipEvent implements ITaskEvent {
    private long pid;
    private int num;

    public static SalvageEquipEvent of(long pid, int num) {
        SalvageEquipEvent event = new SalvageEquipEvent();
        event.pid = pid;
        event.num = num;
        return event;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
        return new TaskChange[]{TaskChange.of(TaskType.SALVAGE_EQUIP, num)};
    }
}
