/**
 * 
 */
package com.xiugou.x1.game.server.module.battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.battle.message.BattleTickMessage;

/**
 * @author YY
 *
 */
@Service
public class BattleService {

	@Autowired
	private PlayerContextManager playerContextManager;
	
	protected void runInSchedule() {
		for(PlayerContext playerContext : playerContextManager.onlines()) {
			playerContext.tell(BattleTickMessage.of(playerContext.getId()));
		}
	}

}
