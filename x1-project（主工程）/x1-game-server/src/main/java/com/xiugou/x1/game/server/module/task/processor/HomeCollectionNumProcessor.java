/**
 * 
 */
package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;

/**
 * @author YY
 *
 */
@Component
public class HomeCollectionNumProcessor extends AbstractTaskEventProcessor {

	@Override
	protected boolean handling(long entityId, Task task, TaskChange change, ITaskConfig taskConfig) {
		if (change.getConditions()[0] == ItemType.MEAT.getThingId()
				|| change.getConditions()[0] == ItemType.WOOD.getThingId()
				|| change.getConditions()[0] == ItemType.MINE.getThingId()) {
			task.setProcess(task.getProcess() + change.getTargetNum());
			task.checkFinish(taskConfig.getTargetNum());
			return true;
		}
		return false;
	}

}
