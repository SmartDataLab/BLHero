/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1009zhigou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ZhiGouLiBaoCache;
import com.xiugou.x1.design.module.autogen.ZhiGouLiBaoAbstractCache.ZhiGouLiBaoCfg;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionLogicService;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.model.ZhiGou;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouLimit;

/**
 * @author yy
 *
 */
@Service
public class ZhiGouPromotionService extends PromotionLogicService {

	@Autowired
	private ZhiGouService zhiGouService;
	@Autowired
	private ZhiGouLiBaoCache zhiGouLiBaoCache;
	
	@Override
	public PromotionLogicType promotionLogicType() {
		return PromotionLogicType.ZHI_GOU;
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
		ZhiGou entity = zhiGouService.getEntity(playerId, typeId);
		for(ZhiGouLimit zhiGouLimit : entity.getLimitBuys().values()) {
			ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(zhiGouLimit.getId());
			if(zhiGouLiBaoCfg.getChargeProductId() <= 0 && zhiGouLimit.getNum() < zhiGouLiBaoCfg.getLimitNum()) {
				return true;
			}
		}
		return false;
	}
}
