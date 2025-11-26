package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.common.InternalCmdRegister.InternalCmdMessage;

import com.xiugou.x1.design.TipsHelper;

public interface PlayerInternalMessage extends InternalCmdMessage {
	
	long getPlayerId();
	
	default void onException(Exception e) {
		TipsHelper.onException(e);
	}
}
