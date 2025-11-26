/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.fakecmd.side.game.PlayerCrossCmdRegister.IPlayerCrossCmdMessage;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.xiugou.x1.design.TipsHelper;

import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class PlayerCrossMessage implements IPlayerCrossCmdMessage {
	private final IPlayerContext playerContext;
	private final int messageId;
	private final int cmd;
	private final ByteString data;

	public PlayerCrossMessage(IPlayerContext playerContext, int messageId, int cmd, ByteString data) {
		this.playerContext = playerContext;
		this.messageId = messageId;
		this.cmd = cmd;
		this.data = data;
	}

	public ByteString getData() {
		return data;
	}

	@Override
	public void onException(Exception e, Message requestObject) {
		TipsMessage builder = TipsHelper.onException(cmd, (Message) requestObject, e);
		playerContext.write(TipsMessage.Proto.ID.getNumber(), builder, messageId);
	}

	@Override
	public void fastResponse() {
		playerContext.write(cmd, data, messageId);
	}

	public IPlayerContext getPlayerContext() {
		return playerContext;
	}

	public int getMessageId() {
		return messageId;
	}

	public int getCmd() {
		return cmd;
	}
}
