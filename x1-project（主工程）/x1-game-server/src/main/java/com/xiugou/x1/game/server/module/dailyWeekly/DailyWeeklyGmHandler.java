package com.xiugou.x1.game.server.module.dailyWeekly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskStatus;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache.DailyWeeklyTaskConfig;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.battle.event.KillMonsterEvent;
import com.xiugou.x1.game.server.module.dailyWeekly.model.DailyTask;
import com.xiugou.x1.game.server.module.dailyWeekly.model.WeeklyTask;
import com.xiugou.x1.game.server.module.dailyWeekly.service.DailyTaskService;
import com.xiugou.x1.game.server.module.dailyWeekly.service.WeeklyTaskService;

/**
 * @author yh
 * @date 2023/6/9
 * @apiNote
 */
@Controller
public class DailyWeeklyGmHandler {
    @Autowired
    private DailyTaskService dailyTaskService;
    @Autowired
    private WeeklyTaskService weeklyTaskService;
    @Autowired
    private DailyWeeklyHandler dailyWeeklyHandler;
    @Autowired
    private DailyWeeklyTaskCache dailyWeeklyTaskCache;

    @PlayerGmCmd(command = "DAILY_TEST_TASK")
    public void dailyTestTask(PlayerContext playerContext, String[] params) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(7000001, 10);
        map.put(7000011, 10);
        EventBus.post(KillMonsterEvent.of(playerContext.getId(), map, Collections.emptyList()));
    }

    @PlayerGmCmd(command = "DAILY_ADD_POINT")
    public void addDailyPoint(PlayerContext playerContext, String[] params) {
        DailyTask entity = dailyTaskService.getEntity(playerContext.getId());
        entity.setDayPoints(entity.getDayPoints() + Integer.parseInt(params[0]));
        dailyTaskService.update(entity);
        dailyWeeklyHandler.pushInfo(playerContext);
    }

    @PlayerGmCmd(command = "WEEKLY_ADD_POINT")
    public void addWEEKLYPoint(PlayerContext playerContext, String[] params) {
        WeeklyTask entity = weeklyTaskService.getEntity(playerContext.getId());
        entity.setWeekPoints(entity.getWeekPoints() + Integer.parseInt(params[0]));
        weeklyTaskService.update(entity);
        dailyWeeklyHandler.pushInfo(playerContext);
    }

    @PlayerGmCmd(command = "WEEKLY_FIN_ALL")
    public void completeWeeklyTask(PlayerContext playerContext, String[] params) {
    	WeeklyTask entity = weeklyTaskService.getEntity(playerContext.getId());
        completeAllTask(entity.getWeeklyTasks());
        weeklyTaskService.update(entity);
        dailyWeeklyHandler.pushInfo(playerContext);
    }

    @PlayerGmCmd(command = "DAILY_FIN_ALL")
    public void completeAllDailyTask(PlayerContext playerContext, String[] params) {
    	DailyTask entity = dailyTaskService.getEntity(playerContext.getId());
        completeAllTask(entity.getDailyTasks());
        dailyTaskService.update(entity);
        dailyWeeklyHandler.pushInfo(playerContext);
    }

    @PlayerGmCmd(command = "DW_FIN_ONE")
    public void completeOneWeeklyTask(PlayerContext playerContext, String[] params) {
        Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
        DailyTask dailyTask = dailyTaskService.getEntity(playerContext.getId());
        WeeklyTask weeklyTask = weeklyTaskService.getEntity(playerContext.getId());
        
        List<Task> dailyTasks = dailyTask.getDailyTasks();
        List<Task> weeklyTasks = weeklyTask.getWeeklyTasks();

        List<Task> tasks = new ArrayList<>();
        tasks.addAll(dailyTasks);
        tasks.addAll(weeklyTasks);

        Task task = tasks.stream().filter(t -> t.getId() == Integer.parseInt(params[0])).findFirst().orElse(null);
        Asserts.isTrue(task != null, TipsCode.GM_PARAM_ERROR);
        Asserts.isTrue(task.getStatus() == TaskStatus.UNDO.getValue(), TipsCode.GM_TASK_COMPLETE);
        completeOneTask(task);
        dailyTaskService.update(dailyTask);
        weeklyTaskService.update(weeklyTask);
        dailyWeeklyHandler.pushInfo(playerContext);
    }


    public void completeAllTask(List<Task> weeklyTasks) {
        for (Task task : weeklyTasks) {
            if (task.getStatus() != TaskStatus.UNDO.getValue()) {
                continue;
            }
            completeOneTask(task);
        }
    }

    public void completeOneTask(Task task) {
        DailyWeeklyTaskConfig dailyWeeklyTaskConfig = dailyWeeklyTaskCache.getOrThrow(task.getId());
        task.setProcess(dailyWeeklyTaskConfig.getTaskTargetNum());
        task.setStatus(TaskStatus.DONE.getValue());
    }

}
