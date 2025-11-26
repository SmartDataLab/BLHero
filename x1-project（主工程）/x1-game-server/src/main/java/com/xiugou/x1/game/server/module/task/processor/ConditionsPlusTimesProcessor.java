package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.stereotype.Component;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 简单的匹配条件的任务处理器
 */
@Component
public class ConditionsPlusTimesProcessor extends AbstractTaskEventProcessor {
	
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
