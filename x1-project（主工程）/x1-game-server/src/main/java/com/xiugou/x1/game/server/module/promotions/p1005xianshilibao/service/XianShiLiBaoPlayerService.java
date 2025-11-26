/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model.XianShiLiBaoPlayer;

/**
 * @author YY
 *
 */
@Service
public class XianShiLiBaoPlayerService extends PlayerOneToOneResetableService<XianShiLiBaoPlayer> {

	@Override
	protected XianShiLiBaoPlayer createWhenNull(long entityId) {
		XianShiLiBaoPlayer entity = new XianShiLiBaoPlayer();
		entity.setPid(entityId);
		return entity;
	}

	@Override
	protected void doDailyReset(XianShiLiBaoPlayer entity) {
		entity.getDailyLimits().clear();
	}
}
