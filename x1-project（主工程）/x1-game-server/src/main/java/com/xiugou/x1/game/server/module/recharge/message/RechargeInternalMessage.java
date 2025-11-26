/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.message;

import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

/**
 * @author YY
 *
 */
public class RechargeInternalMessage implements PlayerInternalMessage {
	private long pid;
	private Recharge recharge;
	
	public static RechargeInternalMessage of(long pid, Recharge recharge) {
		RechargeInternalMessage message = new RechargeInternalMessage();
		message.pid = pid;
		message.recharge = recharge;
		return message;
	}
	
	public long getPlayerId() {
		return pid;
	}
	public Recharge getRecharge() {
		return recharge;
	}
}
