package com.xiugou.x1.game.server.module.dailyWeekly.service;

import java.util.List;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache.DailyWeeklyTaskConfig;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.dailyWeekly.constant.DailyWeeklyEnum;
import com.xiugou.x1.game.server.module.dailyWeekly.model.WeeklyTask;

/**
 * @author yh
 * @date 2023/6/8
 * @apiNote
 */
@Service
public class WeeklyTaskService extends PlayerOneToOneResetableService<WeeklyTask> {

    @Autowired
    private DailyWeeklyTaskCache dailyWeeklyTaskCache;

    @Override
    protected WeeklyTask createWhenNull(long entityId) {
    	WeeklyTask entity = new WeeklyTask();
    	entity.setPid(entityId);
        //给日常周常塞入任务
        List<DailyWeeklyTaskConfig> weeklyTaskConfig = dailyWeeklyTaskCache.getInTypeCollector(DailyWeeklyEnum.WEEKLY.getValue());
        for (DailyWeeklyTaskConfig taskConfig : weeklyTaskConfig) {
        	entity.getWeeklyTasks().add(Task.create(taskConfig.getId()));
        }
        return entity;
    }

    @Override
    protected void doWeeklyReset(WeeklyTask entity) {
        entity.setWeekPoints(0);  //重置积分
        entity.getWeeklyTasks().clear(); //重置任务列表
        entity.getWeekPointReward().clear();//重置活跃宝箱
        List<DailyWeeklyTaskConfig> weeklyTaskConfigs = dailyWeeklyTaskCache.getInTypeCollector(DailyWeeklyEnum.WEEKLY.getValue());
        for (DailyWeeklyTaskConfig weeklyTask : weeklyTaskConfigs) {
            entity.getWeeklyTasks().add(Task.create(weeklyTask.getId()));
        }
    }
}
