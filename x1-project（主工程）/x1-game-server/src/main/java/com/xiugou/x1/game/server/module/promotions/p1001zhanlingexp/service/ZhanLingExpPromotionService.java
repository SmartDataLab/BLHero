/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model.ZhanLingExp;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingExpPromotionService extends PromotionLogicService {

	@Autowired
	private ZhanLingExpService zhanLingExpService;
	
	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.ZHAN_LING_EXP;
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
		ZhanLingExp entity = zhanLingExpService.getEntity(playerId, typeId);
		if(entity.getLevel() > entity.getFreeRewardLv()) {
			return true;
		}
		if(entity.isBuyPremium() && entity.getLevel() > entity.getPremiumRewardLv()) {
			return true;
		}
		return false;
	}
}
