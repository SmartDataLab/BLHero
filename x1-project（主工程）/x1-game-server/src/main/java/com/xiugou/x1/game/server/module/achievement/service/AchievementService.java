/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement.service;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.AchievementTaskCache;
import com.xiugou.x1.design.module.autogen.AchievementTaskAbstractCache.AchievementTaskCfg;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.achievement.model.Achievement;

/**
 * @author hyy
 *
 */
@Service
public class AchievementService extends PlayerOneToOneService<Achievement> {

	@Autowired
	private AchievementTaskCache achievementTaskCache;
	
	@Override
	protected Achievement createWhenNull(long entityId) {
		Achievement achievement = new Achievement();
		achievement.setPid(entityId);
		checkTask(achievement);
		return achievement;
	}

	private boolean checkTask(Achievement achievement) {
		if(achievementTaskCache.all().size() == achievement.getTaskMap().size() + achievement.getOverTasks().size()) {
			return false;
		}
		for(AchievementTaskCfg taskCfg : achievementTaskCache.all()) {
			if(achievement.getOverTasks().contains(taskCfg.getId())) {
				continue;
			}
			if(achievement.getTaskMap().containsKey(taskCfg.getId())) {
				continue;
			}
			Task task = Task.create(taskCfg.getId());
			achievement.getTaskMap().put(task.getId(), task);
		}
		return true;
	}

	@Override
	protected void onGet(Achievement t) {
		if(checkTask(t)) {
			this.update(t);
		}
	}
}
