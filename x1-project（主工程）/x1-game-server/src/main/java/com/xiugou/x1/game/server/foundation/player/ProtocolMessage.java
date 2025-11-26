/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.PlayerCmdRegister.IPlayerCmdMessage;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.xiugou.x1.design.TipsHelper;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class ProtocolMessage implements IPlayerCmdMessage {
	private final PlayerContext playerContext;
	private final MessageWrapper wrapper;

	public ProtocolMessage(PlayerContext playerContext, MessageWrapper wrapper) {
		this.playerContext = playerContext;
		this.wrapper = wrapper;
	}

	public PlayerContext getPlayerContext() {
		return playerContext;
	}

	public MessageWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public int getMessageId() {
		return wrapper.getId();
	}

	@Override
	public int getCmd() {
		return wrapper.getProtoId();
	}

	@Override
	public ByteString getData() {
		return wrapper.getData();
	}

	@Override
	public void onException(Exception e, Message requestObject) {
		TipsMessage builder = TipsHelper.onException(wrapper.getProtoId(), (Message) requestObject, e);
		playerContext.write(TipsMessage.Proto.ID.getNumber(), builder, wrapper.getId());
	}

	@Override
	public void onResponse(Object response) {
		playerContext.write(wrapper.getProtoId(), (GeneratedMessageV3) response, wrapper.getId());
	}
}
