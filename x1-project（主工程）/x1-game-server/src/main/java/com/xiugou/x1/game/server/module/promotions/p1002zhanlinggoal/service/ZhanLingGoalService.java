/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.service;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingGoalCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingGoalAbstractCache.ZhanLingGoalCfg;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model.ZhanLingGoal;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingGoalService extends OneToManyService<ZhanLingGoal> {

	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ZhanLingGoalCache zhanLingGoalCache;
	
	public ZhanLingGoal getEntity(long playerId, int typeId) {
		ZhanLingGoal entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(typeId);
			entity = new ZhanLingGoal();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			entity.setTypeName(activeTemplateCfg.getName());
			this.insert(entity);
		}
		boolean needUpdate = false;
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.setBuyPremium(false);
			entity.getTasks().clear();
			for(ZhanLingGoalCfg cfg : zhanLingGoalCache.getInActivityIdCollector(entity.getTypeId())) {
				Task task = Task.create(cfg.getId());
				entity.getTasks().add(task);
			}
			needUpdate = true;
		}
		if(needUpdate) {
			this.update(entity);
		}
		return entity;
	}
}
