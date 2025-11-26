/**
 *
 */
package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.stereotype.Component;

/**
 * @author YY
 */
@Component
public class KillMonsterProcessor extends AbstractTaskEventProcessor {

    @Override
    protected boolean handling(long rid, Task task, TaskChange change, ITaskConfig taskConfig) {
        if (change.getConditions()[0] == taskConfig.getNeedConditions().get(0)) {
            task.setProcess(task.getProcess() + change.getTargetNum());
            task.checkFinish(taskConfig.getTargetNum());
            return true;
        }
        return false;
    }
}
