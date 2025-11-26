/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement.service;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.task.AbstractTaskSystem;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.AchievementTaskCache;
import com.xiugou.x1.game.server.module.achievement.model.Achievement;
import com.xiugou.x1.game.server.module.task.TaskSystem;

/**
 * @author hyy
 *
 */
@Component
public class AchievementTaskSystem extends AbstractTaskSystem<Achievement> {

	@Autowired
	private AchievementService achievementService;
	@Autowired
	private AchievementTaskCache achievementTaskCache;
	
	@Override
	protected ITaskSystem taskSystem() {
		return TaskSystem.ACHIEVEMENT;
	}

	@Override
	protected List<Achievement> taskContainers(long entityId) {
		return Collections.singletonList(achievementService.getEntity(entityId));
	}

	@Override
	protected void onChangeTask(Achievement taskContainer, ITaskEvent taskEvent, List<Task> changedTasks) {
		achievementService.update(taskContainer);
	}

	@Override
	protected ITaskConfig taskConfig(int taskId) {
		return achievementTaskCache.getOrThrow(taskId);
	}

}
