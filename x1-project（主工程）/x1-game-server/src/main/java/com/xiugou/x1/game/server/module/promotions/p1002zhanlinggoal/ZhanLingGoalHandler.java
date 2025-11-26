/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingGoalCache;
import com.xiugou.x1.design.module.ZhanLingGoalCache.ZhanLingGoalConfig;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingGoalAbstractCache.ZhanLingGoalCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model.ZhanLingGoal;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.service.ZhanLingGoalService;

import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.PbZhanLingGoalCfg;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalCfgRequest;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalCfgResponse;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalInfoRequest;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalInfoResponse;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1002ZhanLingGoal.ZhanLingGoalRewardResponse;

/**
 * @author hyy
 *
 */
@Controller
public class ZhanLingGoalHandler {

	@Autowired
	private ZhanLingGoalCache zhanLingGoalCache;
	@Autowired
	private ZhanLingGoalService zhanLingGoalService;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ThingService thingService;
	
	@PlayerCmd
	public ZhanLingGoalInfoResponse info(PlayerContext playerContext, ZhanLingGoalInfoRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_GOAL.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingGoal entity = zhanLingGoalService.getEntity(playerContext.getId(), request.getTypeId());
	
		ZhanLingGoalInfoResponse.Builder response = ZhanLingGoalInfoResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		for(Task task : entity.getTasks()) {
			response.addTasks(PbHelper.build(task));
		}
		response.addAllPremiumTasks(entity.getPremiumTasks());
		response.setBuyPremium(entity.isBuyPremium());
		return response.build();
	}
	
	@PlayerCmd
	public ZhanLingGoalRewardResponse reward(PlayerContext playerContext, ZhanLingGoalRewardRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_GOAL.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingGoal entity = zhanLingGoalService.getEntity(playerContext.getId(), request.getTypeId());
	
		List<RewardThing> rewardList = new ArrayList<>();
		List<Task> changeTasks = new ArrayList<>();
		List<Integer> premiumRewards = new ArrayList<>();
		for(Task task : entity.getTasks()) {
			if(task.isDone()) {
				ZhanLingGoalCfg zhanLingGoalCfg = zhanLingGoalCache.getOrThrow(task.getId());
				rewardList.addAll(zhanLingGoalCfg.getFreeReward());
				task.setStatus(TaskStatus.EMPTY.getValue());
				changeTasks.add(task);
			}
			if(task.isDone() || task.isEmpty()) {
				ZhanLingGoalCfg zhanLingGoalCfg = zhanLingGoalCache.getOrThrow(task.getId());
				if(entity.isBuyPremium() && !entity.getPremiumTasks().contains(task.getId())) {
					rewardList.addAll(zhanLingGoalCfg.getPremiumReward());
					entity.getPremiumTasks().add(task.getId());
					premiumRewards.add(task.getId());
				}
			}
		}
		
		Asserts.isTrue(!rewardList.isEmpty(), TipsCode.ZL_NO_REWARD);
		
		zhanLingGoalService.update(entity);
		thingService.add(playerContext.getId(), rewardList, GameCause.ZLGOAL_REWARD);
		
		ZhanLingGoalRewardResponse.Builder response = ZhanLingGoalRewardResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		for(Task task : changeTasks) {
			response.addTasks(PbHelper.build(task));
		}
		response.addAllPremiumTasks(premiumRewards);
		return response.build();
	}
	
	@PlayerCmd
	public ZhanLingGoalCfgResponse cfg(PlayerContext playerContext, ZhanLingGoalCfgRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.ZHAN_LING_GOAL.getValue(), TipsCode.ERROR_PARAM);
		
		ZhanLingGoalCfgResponse.Builder response = ZhanLingGoalCfgResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		List<ZhanLingGoalConfig> cfgs = zhanLingGoalCache.getInActivityIdCollector(request.getTypeId());
		for(ZhanLingGoalConfig cfg : cfgs) {
			response.addCfgs(build(cfg));
		}
		return response.build();
	}
	
	
	public PbZhanLingGoalCfg build(ZhanLingGoalCfg cfg) {
		PbZhanLingGoalCfg.Builder builder = PbZhanLingGoalCfg.newBuilder();
		builder.setId(cfg.getId());
		builder.setTaskType(cfg.getTaskType());
		builder.addAllTaskParams(cfg.getTaskParams());
		builder.setTaskTargetNum(cfg.getTaskTargetNum());
		builder.addAllFreeReward(PbHelper.buildReward(cfg.getFreeReward()));
		builder.addAllPremiumReward(PbHelper.buildReward(cfg.getPremiumReward()));
		builder.setSortIndex(cfg.getSortIndex());
		return builder.build();
	}
}
