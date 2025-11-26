/**
 * 
 */
package com.xiugou.x1.game.server.module.player.event;

/**
 * @author YY
 *
 */
public class CostGoldEvent {
	private long playerId;
	
	public static CostGoldEvent of(long playerId) {
		CostGoldEvent event = new CostGoldEvent();
		event.playerId = playerId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}
}
