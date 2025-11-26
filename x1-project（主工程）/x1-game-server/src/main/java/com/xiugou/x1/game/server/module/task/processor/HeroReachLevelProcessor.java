/**
 * 
 */
package com.xiugou.x1.game.server.module.task.processor;

import java.util.List;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;

/**
 * @author YY
 * 
 */
@Component
public class HeroReachLevelProcessor extends AbstractTaskEventProcessor {

	@Autowired
	private HeroService heroService;
	
	@Override
	protected boolean handling(long entityId, Task task, TaskChange change, ITaskConfig taskConfig) {
		List<Hero> heros = heroService.getEntities(entityId);
		int num = 0;
		for(Hero hero : heros) {
			if(hero.getLevel() >= taskConfig.getNeedConditions().get(0)) {
				num += 1;
			}
		}
		if(num > task.getProcess()) {
			task.setProcess(num);
			task.checkFinish(taskConfig.getTargetNum());
			return true;
		}
		return false;
	}

}
