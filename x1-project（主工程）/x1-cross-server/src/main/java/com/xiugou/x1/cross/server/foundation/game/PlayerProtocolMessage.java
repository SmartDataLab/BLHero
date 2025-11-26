/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import org.gaming.fakecmd.side.cross.PlayerCrossCmdRegister.IPlayerCrossCmdMessage;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.xiugou.x1.design.TipsHelper;

import pb.xiugou.x1.protobuf.cross.Cross.PlayerCrossWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class PlayerProtocolMessage implements IPlayerCrossCmdMessage {
	private final GameContext gameContext;
	private final PlayerCrossWrapper wrapper;
	
	public PlayerProtocolMessage(GameContext gameContext, PlayerCrossWrapper wrapper) {
		this.gameContext = gameContext;
		this.wrapper = wrapper;
	}

	public GameContext getGameContext() {
		return gameContext;
	}

	public PlayerCrossWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public long getPlayerId() {
		return wrapper.getPlayerId();
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
		TipsMessage builder = TipsHelper.onException(wrapper.getProtoId(), (Message)requestObject, e);
     	gameContext.writeToPlayer(wrapper.getPlayerId(), TipsMessage.Proto.ID, builder, wrapper.getId(), wrapper.getSyncId());
	}

	@Override
	public void onResponse(Object response) {
		gameContext.writeToPlayer(wrapper.getPlayerId(), wrapper.getProtoId(), (GeneratedMessageV3)response, wrapper.getId(), wrapper.getSyncId());
	}

}
