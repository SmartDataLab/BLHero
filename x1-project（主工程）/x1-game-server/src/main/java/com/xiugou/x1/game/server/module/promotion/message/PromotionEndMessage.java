/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.message;

import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;

/**
 * @author YY
 * 活动结束时的内部消息
 */
public class PromotionEndMessage implements PlayerInternalMessage {
	private long pid;
	private PromotionControl control;
	
	public static PromotionEndMessage of(long pid, PromotionControl control) {
		PromotionEndMessage message = new PromotionEndMessage();
		message.pid = pid;
		message.control = control;
		return message;
	}

	public long getPlayerId() {
		return pid;
	}

	public PromotionControl getControl() {
		return control;
	}
}
