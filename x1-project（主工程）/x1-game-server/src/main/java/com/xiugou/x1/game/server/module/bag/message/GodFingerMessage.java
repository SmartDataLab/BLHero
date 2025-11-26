/**
 * 
 */
package com.xiugou.x1.game.server.module.bag.message;

import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;

/**
 * @author YY
 *
 */
public class GodFingerMessage implements PlayerInternalMessage {
	private long pid;
	private long money;
	
	public static GodFingerMessage of(long pid, long money) {
		GodFingerMessage message = new GodFingerMessage();
		message.pid = pid;
		message.money = money;
		return message;
	}

	public long getPlayerId() {
		return pid;
	}

	public long getMoney() {
		return money;
	}
}
