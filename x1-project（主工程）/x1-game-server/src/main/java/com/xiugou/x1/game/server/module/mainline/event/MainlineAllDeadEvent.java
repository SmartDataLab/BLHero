/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.event;

/**
 * @author YY
 *
 */
public class MainlineAllDeadEvent {
	private long playerId;
	
	public static MainlineAllDeadEvent of(long playerId) {
		MainlineAllDeadEvent event = new MainlineAllDeadEvent();
		event.playerId = playerId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}
}
