/**
 * 
 */
package com.xiugou.x1.simulator.pressing.message;

import com.xiugou.x1.simulator.pressing.PlayerContext;

/**
 * @author YY
 *
 */
public class ConnectToServerMessage {
	private PlayerContext playerContext;
	private String url;

	public ConnectToServerMessage(PlayerContext playerContext, String url) {
		this.playerContext = playerContext;
		this.url = url;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	public String getUrl() {
		return url;
	}
}
