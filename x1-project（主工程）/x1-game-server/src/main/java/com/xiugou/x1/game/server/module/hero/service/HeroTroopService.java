/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.hero.model.HeroTroop;

/**
 * @author YY
 *
 */
@Service
public class HeroTroopService extends PlayerOneToOneService<HeroTroop> {

	@Override
	protected HeroTroop createWhenNull(long entityId) {
		HeroTroop entity = new HeroTroop();
		entity.setPid(entityId);
		return entity;
	}

}
