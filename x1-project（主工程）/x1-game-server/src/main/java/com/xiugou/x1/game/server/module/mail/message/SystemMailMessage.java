/**
 * 
 */
package com.xiugou.x1.game.server.module.mail.message;

import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;

/**
 * @author YY
 *
 */
public class SystemMailMessage implements PlayerInternalMessage {

	private long pid;
	
	public static SystemMailMessage of(long pid) {
		SystemMailMessage message = new SystemMailMessage();
		message.pid = pid;
		return message;
	}

	public long getPlayerId() {
		return pid;
	}
}
