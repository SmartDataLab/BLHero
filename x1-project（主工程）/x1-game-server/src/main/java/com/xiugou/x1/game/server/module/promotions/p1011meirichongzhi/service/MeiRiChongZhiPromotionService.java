/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.model.MeiRiChongZhi;

/**
 * @author yy
 *
 */
@Service
public class MeiRiChongZhiPromotionService extends PromotionLogicService {

	@Autowired
	private MeiRiChongZhiService meiRiChongZhiService;
	
	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.MEI_RI_CHONG_ZHI;
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
		MeiRiChongZhi entity = meiRiChongZhiService.getEntity(playerId, typeId);
		return entity.getCanRewardId() > entity.getTakeRewards().size();
	}
}
