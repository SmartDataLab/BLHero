/**
 * 
 */
package com.xiugou.x1.game.server.module.dungeon.event;

/**
 * @author YY
 *
 */
public class DungeonLoseEvent {
	private long playerId;
	private int dungeonType;
	
	public static DungeonLoseEvent of(long playerId, int dungeonType) {
		DungeonLoseEvent event = new DungeonLoseEvent();
		event.playerId = playerId;
		event.dungeonType = dungeonType;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	public int getDungeonType() {
		return dungeonType;
	}
}
