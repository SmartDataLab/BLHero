package com.xiugou.x1.game.server.module.dailyWeekly.service;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.game.server.module.dailyWeekly.model.WeeklyTask;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;

/**
 * @author yh
 * @date 2023/6/9
 * @apiNote
 */
@Service
public class WeeklyTaskSystem  extends PlayerTaskSystem<WeeklyTask> {
    @Autowired
    private DailyWeeklyTaskCache dailyWeeklyTaskCache;
    @Autowired
    private WeeklyTaskService weeklyTaskService;
    
    @Override
    protected ITaskSystem taskSystem() {
        return TaskSystem.WEEKLY_TASK;
    }

    @Override
    protected ITaskConfig taskConfig(int taskId) {
        return dailyWeeklyTaskCache.getOrThrow(taskId);
    }

    @Override
    protected void saveTaskEntity(WeeklyTask taskContainer, List<Task> changedTasks) {
    	weeklyTaskService.update(taskContainer);
    }
    
	@Override
	protected List<WeeklyTask> taskContainers(long entityId) {
		return Collections.singletonList(weeklyTaskService.getEntity(entityId));
	}
}
