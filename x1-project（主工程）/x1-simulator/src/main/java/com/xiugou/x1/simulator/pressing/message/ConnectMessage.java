/**
 * 
 */
package com.xiugou.x1.simulator.pressing.message;

import com.xiugou.x1.simulator.pressing.PlayerContext;

/**
 * @author YY
 *
 */
public class ConnectMessage {
	public ConnectMessage(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}

	private PlayerContext playerContext;

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

}
