package com.xiugou.x1.game.server.module.dailyWeekly.service;

import java.util.List;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache.DailyWeeklyTaskConfig;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.dailyWeekly.constant.DailyWeeklyEnum;
import com.xiugou.x1.game.server.module.dailyWeekly.model.DailyTask;

/**
 * @author yh
 * @date 2023/6/8
 * @apiNote
 */
@Service
public class DailyTaskService extends PlayerOneToOneResetableService<DailyTask> {

    @Autowired
    private DailyWeeklyTaskCache dailyWeeklyTaskCache;

    @Override
    protected DailyTask createWhenNull(long entityId) {
    	DailyTask entity = new DailyTask();
    	entity.setPid(entityId);
        //给日常周常塞入任务
        List<DailyWeeklyTaskConfig> dailyTaskConfig = dailyWeeklyTaskCache.getInTypeCollector(DailyWeeklyEnum.DAILY.getValue());
        for (DailyWeeklyTaskConfig taskConfig : dailyTaskConfig) {
        	entity.getDailyTasks().add(Task.create(taskConfig.getId()));
        }
        return entity;
    }

    @Override
    protected void doDailyReset(DailyTask entity) {
        entity.setDayPoints(0);  //重置积分
        entity.getDailyTasks().clear(); //重置任务列表
        entity.getDayPointReward().clear();
        List<DailyWeeklyTaskConfig> dailyTaskConfigList = dailyWeeklyTaskCache.getInTypeCollector(DailyWeeklyEnum.DAILY.getValue());
        for (DailyWeeklyTaskConfig dailyTask : dailyTaskConfigList) {
            entity.getDailyTasks().add(Task.create(dailyTask.getId()));
        }
    }
}
