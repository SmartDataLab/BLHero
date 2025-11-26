/**
 * 
 */
package com.xiugou.x1.game.server.module.goldenpig.event;

/**
 * @author YY
 *
 */
public class GoldenPigLoseEvent {
	private long playerId;
	
	public static GoldenPigLoseEvent of(long playerId) {
		GoldenPigLoseEvent event = new GoldenPigLoseEvent();
		event.playerId = playerId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}
}
