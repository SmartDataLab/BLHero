/**
 *
 */
package com.xiugou.x1.game.server.module.battle.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 */
public class KillMonsterEvent implements ITaskEvent {
    private long pid;
    private TaskChange[] taskChanges;

    public static KillMonsterEvent of(long pid, Map<Integer, Integer> monsterMap, List<RewardThing> rewardThings) {
        KillMonsterEvent event = new KillMonsterEvent();
        event.pid = pid;
        List<TaskChange> taskChanges = new ArrayList<>();
        for (Entry<Integer, Integer> entry : monsterMap.entrySet()) {
        	taskChanges.add(TaskChange.of(TaskType.KILL_MONSTER, new int[]{entry.getKey()}, entry.getValue()));
        	taskChanges.add(TaskChange.of(TaskType.KILL_TYPE_MONSTER, new int[]{entry.getKey()}, entry.getValue()));
        }
        for(RewardThing rewardThing : rewardThings) {
			taskChanges.add(TaskChange.of(TaskType.HOME_COLLECTION_RESOURCE, new int[] { rewardThing.getThingId() }, rewardThing.getNum()));
			taskChanges.add(TaskChange.of(TaskType.HOME_COLLECTION_NUM, new int[] { rewardThing.getThingId() }, rewardThing.getNum()));
        }
        event.taskChanges = taskChanges.toArray(new TaskChange[0]);
        return event;
    }

    @Override
    public long getEntityId() {
        return pid;
    }

    @Override
    public TaskChange[] changes() {
        return taskChanges;
    }
}
