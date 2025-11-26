/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.arena.model.ArenaPlayer;

/**
 * @author hyy
 *
 */
@Service
public class ArenaPlayerService extends PlayerOneToOneResetableService<ArenaPlayer> {

	@Override
	protected ArenaPlayer createWhenNull(long entityId) {
		ArenaPlayer arenaPlayer = new ArenaPlayer();
		arenaPlayer.setPid(entityId);
		return arenaPlayer;
	}

	@Override
	protected void doDailyReset(ArenaPlayer entity) {
		entity.setChallengeNum(0);
		entity.setBuyNum(0);
	}
}
