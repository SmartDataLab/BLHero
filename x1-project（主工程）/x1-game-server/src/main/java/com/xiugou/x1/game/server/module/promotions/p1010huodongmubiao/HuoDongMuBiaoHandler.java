/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao;

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
import com.xiugou.x1.design.module.ActivityRewardsCache;
import com.xiugou.x1.design.module.ActivityRewardsCache.ActivityRewardsConfig;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ActivityRewardsAbstractCache.ActivityRewardsCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model.HuoDongMuBiao;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service.HuoDongMuBiaoPromotionService;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service.HuoDongMuBiaoPromotionService.HuoDongMuBiaoParam;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service.HuoDongMuBiaoService;

import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoCfgRequest;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoCfgResponse;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoInfoRequest;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoInfoResponse;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoTakeRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.HuoDongMuBiaoTakeRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1010HuoDongMuBiao.PbHuoDongMuBiaoCfg;
import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author yy
 *
 */
@Controller
public class HuoDongMuBiaoHandler {

	@Autowired
	private HuoDongMuBiaoService huoDongMuBiaoService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private ActivityRewardsCache activityRewardsCache;
	@Autowired
	private HuoDongMuBiaoPromotionService huoDongMuBiaoPromotionService;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	
	@PlayerCmd
	public HuoDongMuBiaoInfoResponse info(PlayerContext playerContext, HuoDongMuBiaoInfoRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.HUO_DONG_MU_BIAO.getValue(), TipsCode.ERROR_PARAM);
		
		HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(playerContext.getId(), request.getTypeId());
		
		HuoDongMuBiaoInfoResponse.Builder response = HuoDongMuBiaoInfoResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		for(Task task : entity.getTasks()) {
			response.addTasks(PbHelper.build(task));
		}
		return response.build();
	}
	
	@PlayerCmd
	public HuoDongMuBiaoTakeRewardResponse takeReward(PlayerContext playerContext, HuoDongMuBiaoTakeRewardRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.HUO_DONG_MU_BIAO.getValue(), TipsCode.ERROR_PARAM);
		
		promotionControlService.assertOpening(applicationSettings.getGameServerId(), request.getTypeId());
		
		HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(playerContext.getId(), request.getTypeId());
		Task targetTask = null;
		for(Task task : entity.getTasks()) {
			if(task.getId() == request.getTaskId()) {
				targetTask = task;
				break;
			}
		}
		Asserts.isTrue(targetTask != null, TipsCode.HDMB_TASK_ERROR);
		Asserts.isTrue(targetTask.isDone(), TipsCode.HDMB_TASK_UN_DONE);
		
		ActivityRewardsCfg activityRewardsCfg = activityRewardsCache.getOrThrow(targetTask.getId());
		List<RewardThing> rewardList = new ArrayList<>();
		rewardList.addAll(activityRewardsCfg.getRewards());
		for(RewardThing select : activityRewardsCfg.getSelectRewards()) {
			if(select.getThingId() == request.getSelectItem()) {
				rewardList.add(select);
				break;
			}
		}
		
		targetTask.setStatus(TaskStatus.EMPTY.getValue());
		boolean allTake = true;
		for(Task task : entity.getTasks()) {
			if(!task.isEmpty()) {
				allTake = false;
				break;
			}
		}
		HuoDongMuBiaoParam param = new HuoDongMuBiaoParam(activeTemplateCfg.getOpenParams());
		boolean addNewTask = false;
		if(allTake && entity.getRewardRound() < param.rewardRound) {
			entity.setRewardRound(entity.getRewardRound() + 1);
			entity.getTasks().clear();
			List<ActivityRewardsConfig> cfgList = activityRewardsCache.getInTemplateIdCollector(entity.getTypeId());
			for(ActivityRewardsConfig cfg : cfgList) {
				entity.getTasks().add(Task.create(cfg.getId()));
			}
			addNewTask = true;
		}
		huoDongMuBiaoService.update(entity);
		
		thingService.add(playerContext.getId(), rewardList, GameCause.HDMB_TASK_REWARD, entity.getTypeName());
		
		PromotionRedPointMessage.Builder message = PromotionRedPointMessage.newBuilder();
		message.setTypeId(request.getTypeId());
		message.setRedPoint(huoDongMuBiaoPromotionService.showLoginRedPoint(playerContext.getId(), request.getTypeId()));
		playerContextManager.push(playerContext.getId(), PromotionRedPointMessage.Proto.ID, message.build());
		
		
		HuoDongMuBiaoTakeRewardResponse.Builder response = HuoDongMuBiaoTakeRewardResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		response.setTasks(PbHelper.build(targetTask));
		if(addNewTask) {
			for(Task task : entity.getTasks()) {
				response.addNewTasks(PbHelper.build(task));
			}
		}
		return response.build();
	}
	
	@PlayerCmd
	public HuoDongMuBiaoCfgResponse cfg(PlayerContext playerContext, HuoDongMuBiaoCfgRequest request) {
		List<ActivityRewardsConfig> cfgs = activityRewardsCache.findInTemplateIdCollector(request.getTypeId());
		
		HuoDongMuBiaoCfgResponse.Builder response = HuoDongMuBiaoCfgResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		if(cfgs != null) {
			for(ActivityRewardsConfig cfg : cfgs) {
				PbHuoDongMuBiaoCfg.Builder data = PbHuoDongMuBiaoCfg.newBuilder();
				data.setId(cfg.getId());
				data.setIndex(cfg.getIndex());
				data.setTaskTargetNum(cfg.getTaskTargetNum());
				data.addAllSelectRewards(PbHelper.buildReward(cfg.getSelectRewards()));
				data.addAllRewards(PbHelper.buildReward(cfg.getRewards()));
				data.setDesc(cfg.getDesc());
				response.addCfgs(data.build());
			}
		}
		return response.build();
	}
}
