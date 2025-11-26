/**
 * 
 */
package com.xiugou.x1.game.server.module.battle.message;

import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;

/**
 * @author YY
 *
 */
public class BattleTickMessage implements PlayerInternalMessage {
	private final long playerId;

	private BattleTickMessage(long playerId) {
		this.playerId = playerId;
	}
	public static BattleTickMessage of(long playerId) {
		return new BattleTickMessage(playerId);
	}
	
	@Override
	public long getPlayerId() {
		return playerId;
	}
}
