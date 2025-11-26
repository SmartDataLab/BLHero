/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ActivityRewardsCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ActivityRewardsAbstractCache.ActivityRewardsCfg;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model.HuoDongMuBiao;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;

import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author yy
 *
 */
@Service
public class HuoDongMuBiaoTaskService extends PlayerTaskSystem<HuoDongMuBiao> {

	@Autowired
	private ActivityRewardsCache activityRewardsCache;
	@Autowired
	private HuoDongMuBiaoService huoDongMuBiaoService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	
	@Override
	protected ITaskSystem taskSystem() {
		return TaskSystem.HDMB_TASK;
	}

	@Override
	protected void saveTaskEntity(HuoDongMuBiao taskContainer, List<Task> changedTasks) {
		huoDongMuBiaoService.update(taskContainer);
	}
	
	@Override
	protected List<HuoDongMuBiao> taskContainers(long entityId) {
		List<HuoDongMuBiao> list = new ArrayList<>();
		
		List<ActiveTemplateCfg> templateList = activeTemplateCache.getInLogicTypeCollector(PromotionLogicType.HUO_DONG_MU_BIAO.getValue());
		for(ActiveTemplateCfg cfg : templateList) {
			if(!promotionControlService.isRunning(applicationSettings.getGameServerId(), cfg.getId())) {
				continue;
			}
			HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(entityId, cfg.getId());
			list.add(entity);
		}
		return list;
	}

	@Override
	protected ITaskConfig taskConfig(int taskId) {
		return activityRewardsCache.getOrThrow(taskId);
	}

	@Override
	protected void afterPushChangeTask(long playerId, List<Task> changedTasks) {
		//已经推送过的活动
		Set<Integer> pushed = new HashSet<>();
		for(Task task : changedTasks) {
			if(!task.isDone()) {
				continue;
			}
			ActivityRewardsCfg activityRewardsCfg = activityRewardsCache.getOrThrow(task.getId());
			if(pushed.contains(activityRewardsCfg.getTemplateId())) {
				continue;
			}
			pushed.add(activityRewardsCfg.getTemplateId());
			
			PromotionRedPointMessage.Builder builder = PromotionRedPointMessage.newBuilder();
			builder.setTypeId(activityRewardsCfg.getTemplateId());
			builder.setRedPoint(true);
			playerContextManager.push(playerId, PromotionRedPointMessage.Proto.ID, builder.build());
		}
	}
}
