/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.IPlayer;

/**
 * @author hyy
 *
 */
public class PlayerJump implements IPlayer {
	private long id;
	private int serverZone;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerZone() {
		return serverZone;
	}
	public void setServerZone(int serverZone) {
		this.serverZone = serverZone;
	}
}
