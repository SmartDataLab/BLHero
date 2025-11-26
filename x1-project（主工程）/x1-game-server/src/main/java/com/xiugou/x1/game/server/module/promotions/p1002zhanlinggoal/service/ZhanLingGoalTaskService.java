/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingGoalCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model.ZhanLingGoal;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingGoalTaskService extends PlayerTaskSystem<ZhanLingGoal> {

	@Autowired
	private ZhanLingGoalCache zhanLingGoalCache;
	@Autowired
	private ZhanLingGoalService zhanLingGoalService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	protected ITaskSystem taskSystem() {
		return TaskSystem.ZHANLING_TASK;
	}

	@Override
	protected ITaskConfig taskConfig(int taskId) {
		return zhanLingGoalCache.getOrThrow(taskId);
	}

	@Override
	protected List<ZhanLingGoal> taskContainers(long entityId) {
		List<ZhanLingGoal> list = new ArrayList<>();
		
		List<ActiveTemplateCfg> templateList = activeTemplateCache.getInLogicTypeCollector(PromotionLogicType.ZHAN_LING_GOAL.getValue());
		for(ActiveTemplateCfg cfg : templateList) {
			if(!promotionControlService.isRunning(applicationSettings.getGameServerId(), cfg.getId())) {
				continue;
			}
			ZhanLingGoal entity = zhanLingGoalService.getEntity(entityId, cfg.getId());
			list.add(entity);
		}
		return list;
	}
	
	@Override
	protected void saveTaskEntity(ZhanLingGoal taskContainer, List<Task> changedTasks) {
		zhanLingGoalService.update(taskContainer);
	}

}
