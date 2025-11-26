/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import org.gaming.fakecmd.side.cross.CrossCmdRegister.ICrossCmdMessage;

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
public class ProtocolMessage implements ICrossCmdMessage {
	private final GameContext gameContext;
	private final MessageWrapper wrapper;

	public ProtocolMessage(GameContext gameContext, MessageWrapper wrapper) {
		this.gameContext = gameContext;
		this.wrapper = wrapper;
	}

	public MessageWrapper getWrapper() {
		return wrapper;
	}

	public GameContext getGameContext() {
		return gameContext;
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
		gameContext.write(TipsMessage.Proto.ID.getNumber(), builder, wrapper.getId());
	}

	@Override
	public void onResponse(Object response) {
		gameContext.write(wrapper.getProtoId(), (GeneratedMessageV3)response, wrapper.getId());
	}
}
