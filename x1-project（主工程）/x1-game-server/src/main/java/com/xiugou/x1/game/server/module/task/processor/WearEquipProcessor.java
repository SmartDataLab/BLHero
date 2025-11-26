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

import com.xiugou.x1.game.server.module.equip.model.EquipWear;
import com.xiugou.x1.game.server.module.equip.service.EquipWearService;

/**
 * @author YY
 *
 */
@Component
public class WearEquipProcessor extends AbstractTaskEventProcessor {

	@Autowired
	private EquipWearService equipWearService;
	
	@Override
	protected boolean handling(long entityId, Task task, TaskChange change, ITaskConfig taskConfig) {
		EquipWear equipWear = equipWearService.getEntity(entityId);
		int count = 0;
		for(long equipId : equipWear.getWearing().values()) {
			if(equipId > 0) {
				count += 1;
			}
		}
		if(count > task.getProcess()) {
			task.setProcess(count);
			task.checkFinish(taskConfig.getTargetNum());
			return true;
		}
		return false;
	}

}