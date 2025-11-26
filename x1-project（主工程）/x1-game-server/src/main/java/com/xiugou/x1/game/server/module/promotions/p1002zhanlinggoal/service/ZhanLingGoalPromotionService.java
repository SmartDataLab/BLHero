/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.service;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.promotions.p1002zhanlinggoal.model.ZhanLingGoal;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingGoalPromotionService extends PromotionLogicService {

	@Autowired
	private ZhanLingGoalService zhanLingGoalService;
	
	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.ZHAN_LING_GOAL;
	}

	@Override
	public void whenStart(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void whenStill(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void whenEnd(PromotionControl control) {
		//什么都不用做
	}

	@Override
	public void handlePromotionEnd(long playerId, PromotionControl control) {
		//什么都不用做
	}

	@Override
	public boolean showLoginRedPoint(long playerId, int typeId) {
		ZhanLingGoal entity = zhanLingGoalService.getEntity(playerId, typeId);
		for(Task task : entity.getTasks()) {
			if(task.isDone()) {
				return true;
			}
			if(task.isEmpty() && entity.isBuyPremium() && !entity.getPremiumTasks().contains(task.getId())) {
				return true;
			}
		}
		return false;
	}
}
