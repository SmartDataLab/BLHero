/**
 *
 */
package com.xiugou.x1.game.server.module.mainline.service;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.ITaskType;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.gaming.prefab.task.TaskWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.MainlineTaskCache;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
@Service
public class MainlineTaskSystem extends PlayerTaskSystem<MainlinePlayer> {

	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	@Autowired
	private MainlineTaskCache mainlineTaskCache;

	@Override
	protected ITaskSystem taskSystem() {
		return TaskSystem.MAINLINE;
	}

	@Override
	protected ITaskConfig taskConfig(int taskId) {
		return mainlineTaskCache.getOrThrow(taskId);
	}

	@Override
	protected void saveTaskEntity(MainlinePlayer taskContainer, List<Task> changedTasks) {
		mainlinePlayerService.update(taskContainer);
	}

	@Override
	protected List<MainlinePlayer> taskContainers(long entityId) {
		return Collections.singletonList(mainlinePlayerService.getEntity(entityId));
	}
	
	public void check(long playerId, Task task) {
		if (task.isDone() || task.isEmpty()) {
			return;
		}
		ITaskConfig iTaskConfig = taskConfig(task.getId());
		ITaskType taskType = TaskType.valueFor(iTaskConfig.getIdentity());
		if (taskType != TaskType.HAVE_MINE && taskType != TaskType.HAVE_MEAT && taskType != TaskType.HAVE_WOOD) {
			// 对这几个类型的主线任务做特殊处理，恶心得一逼
			return;
		}
		TaskWrapper wrapper = TaskWrapper.of(task, iTaskConfig);
		TaskChange change = TaskChange.ofZero(taskType);

		AbstractTaskEventProcessor processor = AbstractTaskEventProcessor.getProcessor(change.getTaskType());
		processor.handle(playerId, wrapper.getTask(), change, wrapper.getTaskConfig());
	}
}
