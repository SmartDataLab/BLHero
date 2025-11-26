package com.xiugou.x1.game.server.module.dailyWeekly;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskStatus;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.DailyWeeklyGiftBoxCache;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache;
import com.xiugou.x1.design.module.DailyWeeklyTaskCache.DailyWeeklyTaskConfig;
import com.xiugou.x1.design.module.autogen.DailyWeeklyGiftBoxAbstractCache.DailyWeeklyGiftBoxCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.dailyWeekly.constant.DailyWeeklyEnum;
import com.xiugou.x1.game.server.module.dailyWeekly.model.DailyTask;
import com.xiugou.x1.game.server.module.dailyWeekly.model.WeeklyTask;
import com.xiugou.x1.game.server.module.dailyWeekly.service.DailyTaskService;
import com.xiugou.x1.game.server.module.dailyWeekly.service.WeeklyTaskService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.dailyweeklytask.DailyWeeklyTask.DailyWeeklyBoxRequest;
import pb.xiugou.x1.protobuf.dailyweeklytask.DailyWeeklyTask.DailyWeeklyBoxResponse;
import pb.xiugou.x1.protobuf.dailyweeklytask.DailyWeeklyTask.DailyWeeklyInfoResponse;
import pb.xiugou.x1.protobuf.dailyweeklytask.DailyWeeklyTask.DailyWeeklyRewardRequest;
import pb.xiugou.x1.protobuf.dailyweeklytask.DailyWeeklyTask.DailyWeeklyRewardResponse;

/**
 * @author yh
 * @date 2023/6/9
 * @apiNote
 */
@Controller
public class DailyWeeklyHandler extends AbstractModuleHandler {

	@Autowired
	private DailyTaskService dailyTaskService;
	@Autowired
	private WeeklyTaskService weeklyTaskService;
	@Autowired
	private DailyWeeklyTaskCache dailyWeeklyTaskCache;
	@Autowired
	private DailyWeeklyGiftBoxCache dailyWeeklyGiftBoxCache;
	@Autowired
	private ThingService thingService;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		DailyTask dailyTask = dailyTaskService.getEntity(playerId);
		WeeklyTask weeklyTask = weeklyTaskService.getEntity(playerId);
		
		DailyWeeklyInfoResponse.Builder response = DailyWeeklyInfoResponse.newBuilder();
		
		response.setDayPoints((int)dailyTask.getDayPoints());
		response.setWeekPoints((int)weeklyTask.getWeekPoints());
		// 已领日常活跃宝箱列表
		response.addAllDayPointReward(dailyTask.getDayPointReward());
		// 已领周常活跃宝箱列表
		response.addAllWeekPointReward(weeklyTask.getWeekPointReward());
		// 日常任务列表
		List<Task> dailyTasks = dailyTask.getDailyTasks();
		for (Task task : dailyTasks) {
			response.addDailyTasks(PbHelper.build(task));
		}
		// 周常任务列表
		List<Task> weeklyTasks = weeklyTask.getWeeklyTasks();
		for (Task task : weeklyTasks) {
			response.addWeeklyTasks(PbHelper.build(task));
		}
		// 日常周常重置
		response.setDailyResetTime(LocalDateTimeUtil.toEpochMilli(dailyTask.getDailyTime()));
		response.setWeeklyResetTime(LocalDateTimeUtil.toEpochMilli(weeklyTask.getWeeklyTime()));
		playerContextManager.push(playerId, DailyWeeklyInfoResponse.Proto.ID, response.build());
	}

	/**
	 * 任务奖励
	 */
	@PlayerCmd
	public DailyWeeklyRewardResponse dailyWeeklyReward(PlayerContext playerContext, DailyWeeklyRewardRequest request) {
		int type = request.getType();
		
		if (type == DailyWeeklyEnum.DAILY.getValue()) {
			return getDailyReward(playerContext.getId());
		} else {
			return getWeeklyReward(playerContext.getId());
		}
	}
	
	private DailyWeeklyRewardResponse getDailyReward(long playerId) {
		DailyTask dailyTask = dailyTaskService.getEntity(playerId);
		List<RewardThing> rewardList = new ArrayList<>();
		
		DailyWeeklyRewardResponse.Builder response = DailyWeeklyRewardResponse.newBuilder();
		for (Task task : dailyTask.getDailyTasks()) {
			if (!task.isDone()) {
				continue;
			}
			DailyWeeklyTaskConfig dailyWeeklyTaskConfig = dailyWeeklyTaskCache.getOrThrow(task.getId());
			rewardList.addAll(dailyWeeklyTaskConfig.getRewardPoints());
			
			task.setStatus(TaskStatus.EMPTY.getValue());
			response.addTasks(PbHelper.build(task));
		}
		
		dailyTaskService.update(dailyTask);
		thingService.add(playerId, rewardList, GameCause.DAILY_WEEKLY_TASK);
		
		response.setPoints((int)dailyTask.getDayPoints());
		response.setType(DailyWeeklyEnum.DAILY.getValue());
		return response.build();
	}
	
	private DailyWeeklyRewardResponse getWeeklyReward(long playerId) {
		WeeklyTask weeklyTask = weeklyTaskService.getEntity(playerId);
		List<RewardThing> rewardList = new ArrayList<>();
		
		DailyWeeklyRewardResponse.Builder response = DailyWeeklyRewardResponse.newBuilder();
		for (Task task : weeklyTask.getWeeklyTasks()) {
			if (!task.isDone()) {
				continue;
			}
			DailyWeeklyTaskConfig dailyWeeklyTaskConfig = dailyWeeklyTaskCache.getOrThrow(task.getId());
			rewardList.addAll(dailyWeeklyTaskConfig.getRewardPoints());
			
			task.setStatus(TaskStatus.EMPTY.getValue());
			response.addTasks(PbHelper.build(task));
		}
		
		weeklyTaskService.update(weeklyTask);
		thingService.add(playerId, rewardList, GameCause.DAILY_WEEKLY_TASK);
		
		response.setPoints((int)weeklyTask.getWeekPoints());
		response.setType(DailyWeeklyEnum.WEEKLY.getValue());
		return response.build();
	}

	@PlayerCmd
	public DailyWeeklyBoxResponse dailyWeeklyBox(PlayerContext playerContext, DailyWeeklyBoxRequest request) {
		if (request.getType() == DailyWeeklyEnum.DAILY.getValue()) {
			return getDailyBox(playerContext.getId());
		} else {
			return getWeeklyBox(playerContext.getId());
		}
	}
	
	private DailyWeeklyBoxResponse getDailyBox(long playerId) {
		DailyTask dailyTask = dailyTaskService.getEntity(playerId);
		
		List<Integer> pointRewardList = dailyTask.getDayPointReward();
		int points = (int)dailyTask.getDayPoints();

		List<RewardThing> rewardList = new ArrayList<>();
		List<DailyWeeklyGiftBoxCfg> giftBoxCfg = dailyWeeklyGiftBoxCache.getInTypeCollector(DailyWeeklyEnum.DAILY.getValue());
		
		DailyWeeklyBoxResponse.Builder response = DailyWeeklyBoxResponse.newBuilder();
		for (DailyWeeklyGiftBoxCfg config : giftBoxCfg) {
			if (config.getPoints() > points) {
				break;
			}
			if (pointRewardList.contains(config.getRewardNum())) {
				continue;
			}
			rewardList.addAll(config.getRewards());
			pointRewardList.add(config.getRewardNum());
			
			response.addBoxNums(config.getRewardNum());
		}
		
		dailyTaskService.update(dailyTask);
		thingService.add(playerId, rewardList, GameCause.DAILY_WEEKLY_ACTIVE_BOX_REWARD);

		response.setType(DailyWeeklyEnum.DAILY.getValue());
		return response.build();
	}
	
	private DailyWeeklyBoxResponse getWeeklyBox(long playerId) {
		WeeklyTask weeklyTask = weeklyTaskService.getEntity(playerId);
		
		List<Integer> pointRewardList = weeklyTask.getWeekPointReward();
		int points = (int)weeklyTask.getWeekPoints();

		List<RewardThing> rewardList = new ArrayList<>();
		List<DailyWeeklyGiftBoxCfg> giftBoxCfg = dailyWeeklyGiftBoxCache.getInTypeCollector(DailyWeeklyEnum.WEEKLY.getValue());
		
		DailyWeeklyBoxResponse.Builder response = DailyWeeklyBoxResponse.newBuilder();
		for (DailyWeeklyGiftBoxCfg config : giftBoxCfg) {
			if (config.getPoints() > points) {
				break;
			}
			if (pointRewardList.contains(config.getRewardNum())) {
				continue;
			}
			rewardList.addAll(config.getRewards());
			pointRewardList.add(config.getRewardNum());
			
			response.addBoxNums(config.getRewardNum());
		}
		
		weeklyTaskService.update(weeklyTask);
		thingService.add(playerId, rewardList, GameCause.DAILY_WEEKLY_ACTIVE_BOX_REWARD);

		response.setType(DailyWeeklyEnum.WEEKLY.getValue());
		return response.build();
	}
}
