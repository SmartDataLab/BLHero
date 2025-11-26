/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.event;

/**
 * @author YY
 *
 */
public class TowerLoseEvent {
	private long playerId;
	
	public static TowerLoseEvent of(long playerId) {
		TowerLoseEvent event = new TowerLoseEvent();
		event.playerId = playerId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}
}
