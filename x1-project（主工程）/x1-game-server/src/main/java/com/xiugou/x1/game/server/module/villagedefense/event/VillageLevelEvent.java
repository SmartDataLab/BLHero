package com.xiugou.x1.game.server.module.villagedefense.event;


import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/8/17
 * @apiNote 村庄保卫通关事件
 */
public class VillageLevelEvent implements ITaskEvent {
    private long pid;
    private int level;

    public static VillageLevelEvent of(long pid, int level) {
        VillageLevelEvent event = new VillageLevelEvent();
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
        return new TaskChange[]{TaskChange.of(TaskType.COMPLETE_DUPLICATE, new int[]{BattleType.VILLAGE_DEFENSE.getValue()}, 1)};
    }
}
