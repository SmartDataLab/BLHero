/**
 * 
 */
package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.trainingCamp.service.TrainingCampService;

/**
 * @author YY
 *
 */
@Component
public class TroopNumProcessor extends AbstractTaskEventProcessor {

	@Autowired
	private TrainingCampService trainingCampService;
	
	@Override
	protected boolean handling(long entityId, Task task, TaskChange change, ITaskConfig taskConfig) {
		int troopNum = trainingCampService.getMaxTroopNum(entityId);
		if(troopNum > task.getProcess()) {
			task.setProcess(troopNum);
	        task.checkFinish(taskConfig.getTargetNum());
	        return true;
		}
		return false;
	}

}
