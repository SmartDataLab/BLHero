package com.xiugou.x1.game.server.module.task.processor;

import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.MonsterCache.*;
import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Component
public class KillMonsterTypeProcessor  extends AbstractTaskEventProcessor {
    @Autowired
    private MonsterCache monsterCache;
    @Override
    protected boolean handling(long rid, Task task, TaskChange change, ITaskConfig taskConfig) {
        MonsterConfig monsterConfig = monsterCache.getOrThrow(change.getConditions()[0]);
        if (monsterConfig.getType() == taskConfig.getNeedConditions().get(0)) {
            task.setProcess(task.getProcess() + change.getTargetNum());
            task.checkFinish(taskConfig.getTargetNum());
            return true;
        }
        return false;
    }
}
