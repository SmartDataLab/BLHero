package com.xiugou.x1.game.server.module.dailyWeekly.service;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.game.server.module.dailyWeekly.model.DailyTask;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;

/**
 * @author yh
 * @date 2023/6/8
 * @apiNote
 */
@Service
public class DailyTaskSystem extends PlayerTaskSystem<DailyTask> {
    @Autowired
    private DailyWeeklyTaskCache dailyWeeklyTaskCache;
    @Autowired
    private DailyTaskService dailyTaskService;

    @Override
    protected ITaskSystem taskSystem() {
        return TaskSystem.DAILY_TASK;
    }

    @Override
    protected ITaskConfig taskConfig(int taskId) {
         return dailyWeeklyTaskCache.getOrThrow(taskId);
    }

    @Override
    protected void saveTaskEntity(DailyTask taskContainer, List<Task> changedTasks) {
    	dailyTaskService.update(taskContainer);
    }

	@Override
	protected List<DailyTask> taskContainers(long entityId) {
		return Collections.singletonList(dailyTaskService.getEntity(entityId));
	}
}
