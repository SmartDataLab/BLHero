/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.AchievementPointsCache;
import com.xiugou.x1.design.module.AchievementTaskCache;
import com.xiugou.x1.design.module.AchievementTaskCache.AchievementTaskConfig;
import com.xiugou.x1.design.module.autogen.AchievementPointsAbstractCache.AchievementPointsCfg;
import com.xiugou.x1.design.module.autogen.AchievementTaskAbstractCache.AchievementTaskCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.achievement.model.Achievement;
import com.xiugou.x1.game.server.module.achievement.service.AchievementService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.achievement.Achievement.AchievementInfoResponse;
import pb.xiugou.x1.protobuf.achievement.Achievement.AchievementPointRewardRequest;
import pb.xiugou.x1.protobuf.achievement.Achievement.AchievementPointRewardResponse;
import pb.xiugou.x1.protobuf.achievement.Achievement.AchievementTaskRewardRequest;
import pb.xiugou.x1.protobuf.achievement.Achievement.AchievementTaskRewardResponse;

/**
 * @author hyy
 *
 */
@Controller
public class AchievementHandler extends AbstractModuleHandler {

	@Autowired
	private AchievementService achievementService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private AchievementTaskCache achievementTaskCache;
	@Autowired
	private AchievementPointsCache achievementPointsCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		Achievement achievement = achievementService.getEntity(playerContext.getId());
		
		AchievementInfoResponse.Builder response = AchievementInfoResponse.newBuilder();
		response.setPoint(achievement.getPoint());
		response.setPointRewardId(achievement.getPointRewardId());
		response.addAllOverTasks(achievement.getOverTasks());
		for(Entry<String, ArrayList<AchievementTaskConfig>> entry : achievementTaskCache.getTaskTypeCollector().entrySet()) {
			for(AchievementTaskConfig taskConfig : entry.getValue()) {
				Task task = achievement.getTaskMap().get(taskConfig.getId());
				if(task != null) {
					response.addTasks(PbHelper.build(task));
					break;
				}
			}
		}
		playerContextManager.push(playerContext.getId(), AchievementInfoResponse.Proto.ID, response.build());
	}
	
	@PlayerCmd
	public AchievementTaskRewardResponse taskReward(PlayerContext playerContext, AchievementTaskRewardRequest request) {
		Achievement achievement = achievementService.getEntity(playerContext.getId());
		Asserts.isTrue(achievement.getOverTasks().contains(request.getTaskId()), TipsCode.ACHIEVEMENT_TASK_REWARD_TOOK);
		Task task = achievement.getTaskMap().get(request.getTaskId());
		Asserts.isTrue(task != null, TipsCode.ACHIEVEMENT_TASK_REWARD_TOOK);
		Asserts.isTrue(task.isDone(), TipsCode.ACHIEVEMENT_TASK_UN_DONE);
		
		AchievementTaskCfg achievementTaskCfg = achievementTaskCache.getOrThrow(task.getId());
		
		task.markReceived();
		achievement.getTaskMap().remove(task.getId());
		achievement.getOverTasks().add(task.getId());
		achievementService.update(achievement);
		
		AchievementTaskConfig nextCfg = achievementTaskCache.findInPreTaskIndex(task.getId());
		Task nextTask = null;
		if(nextCfg != null) {
			nextTask = achievement.getTaskMap().get(nextCfg.getId());
		}
		
		thingService.add(playerContext.getId(), achievementTaskCfg.getRewards(), GameCause.ACHIEVEMENT_TASK);
		
		AchievementTaskRewardResponse.Builder response = AchievementTaskRewardResponse.newBuilder();
		response.setTaskId(task.getId());
		if(nextTask != null) {
			response.setNextTask(PbHelper.build(nextTask));
		}
		return response.build();
	}
	
	@PlayerCmd
	public AchievementPointRewardResponse pointReward(PlayerContext playerContext, AchievementPointRewardRequest request) {
		Achievement achievement = achievementService.getEntity(playerContext.getId());
		AchievementPointsCfg achievementPointsCfg = achievementPointsCache.getOrNull(achievement.getPointRewardId() + 1);
		Asserts.isTrue(achievementPointsCfg != null, TipsCode.ACHIEVEMENT_POINT_NO_MORE);
		Asserts.isTrue(achievement.getPoint() >= achievementPointsCfg.getNeedPoint(), TipsCode.ACHIEVEMENT_POINT_LACK);
		
		achievement.setPointRewardId(achievement.getPointRewardId() + 1);
		achievementService.update(achievement);
		
		thingService.add(playerContext.getId(), achievementPointsCfg.getRewards(), GameCause.ACHIEVEMENT_POINT);
		
		AchievementPointRewardResponse.Builder response = AchievementPointRewardResponse.newBuilder();
		response.setPointRewardId(achievement.getPointRewardId());
		return response.build();
	}
}
