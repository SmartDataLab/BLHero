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
 * 简单的计算累计次数的任务处理器
 */
@Component
public class SimpleCountNumProcessor extends AbstractTaskEventProcessor {

    @Override
    protected boolean handling(long rid, Task task, TaskChange change, ITaskConfig taskConfig) {
        task.setProcess(task.getProcess() + change.getTargetNum());
        task.checkFinish(taskConfig.getTargetNum());
        return true;
    }
}
