package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.stereotype.Component;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote 简单的次数大于替换的任务处理器
 */
@Component
public class SimpleChangeNumProcessor extends AbstractTaskEventProcessor {
	
	@Override
	protected boolean handling(long rid, Task task, TaskChange change, ITaskConfig taskConfig) {
		if(change.getTargetNum() > task.getProcess()) {
			task.setProcess(change.getTargetNum());
			task.checkFinish(taskConfig.getTargetNum());
			return true;
		}
		return false;
	}
}
