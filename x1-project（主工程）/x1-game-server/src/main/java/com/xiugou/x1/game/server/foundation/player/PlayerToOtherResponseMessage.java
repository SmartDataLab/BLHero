/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.IPlayer;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.fakecmd.side.game.IPlayerToOtherMessage;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.xiugou.x1.design.TipsHelper;

import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherResponseWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class PlayerToOtherResponseMessage implements IPlayerToOtherMessage {
	private IPlayerContext playerContext;
	private PlayerJump otherPlayer;
	private final PlayerToOtherResponseWrapper wrapper;

	public PlayerToOtherResponseMessage(IPlayerContext playerContext, PlayerToOtherResponseWrapper wrapper) {
		this.wrapper = wrapper;
		
		if(playerContext != null) {
			this.playerContext = playerContext;
		} else {
			PlayerJumpContext jumpContext = new PlayerJumpContext();
			jumpContext.setId(wrapper.getToPlayerId());
			jumpContext.setServerZone(wrapper.getToServerId());
			jumpContext.setCurrMsgId(wrapper.getId());
			this.playerContext = jumpContext;
		}
		
		this.otherPlayer = new PlayerJump();
		otherPlayer.setId(wrapper.getFromPlayerId());
		otherPlayer.setServerZone(wrapper.getFromServerId());
	}

	public PlayerToOtherResponseWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public IPlayerContext getPlayerContext() {
		return playerContext;
	}

	@Override
	public IPlayer getPlayer() {
		return otherPlayer;
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
		playerContext.write(TipsMessage.Proto.ID.getNumber(), builder, wrapper.getProtoId());
	}

	@Override
	public void onResponse(Object response) {
		playerContext.write(wrapper.getProtoId(), (GeneratedMessageV3) response, wrapper.getId());
	}

	@Override
	public void fastResponse() {
		System.out.println("fastResponse " + wrapper.getId());
		playerContext.write(wrapper.getProtoId(), wrapper.getData(), wrapper.getId());
	}
}
