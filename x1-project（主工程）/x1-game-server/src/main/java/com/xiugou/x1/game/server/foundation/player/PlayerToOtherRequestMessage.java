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
import com.xiugou.x1.game.server.foundation.cross.CrossContextManager;

import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherRequestWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class PlayerToOtherRequestMessage implements IPlayerToOtherMessage {
	private IPlayerContext playerContext;
	private PlayerJump otherPlayer;
	private final PlayerToOtherRequestWrapper wrapper;

	public PlayerToOtherRequestMessage(PlayerToOtherRequestWrapper wrapper) {
		PlayerJumpContext jumpContext = new PlayerJumpContext();
		jumpContext.setId(wrapper.getToPlayerId());
		jumpContext.setServerZone(wrapper.getToServerId());
		jumpContext.setCurrMsgId(wrapper.getId());
		System.out.println("PlayerToOtherRequestMessage " + jumpContext.getCurrMsgId());
		this.playerContext = jumpContext;
		
		this.otherPlayer = new PlayerJump();
		otherPlayer.setId(wrapper.getFromPlayerId());
		otherPlayer.setServerZone(wrapper.getFromServerId());
		
		this.wrapper = wrapper;
	}

	public PlayerToOtherRequestWrapper getWrapper() {
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
		//当跳转信息是请求跳转时，需要回复的玩家应该是fromPlayer
		CrossContextManager.jumpResponse(playerContext, otherPlayer, TipsMessage.Proto.ID, builder);
	}

	@Override
	public void onResponse(Object response) {
		//当跳转信息是请求跳转时，需要回复的玩家应该是fromPlayer
		System.out.println("onResponse " + playerContext.getClass().getSimpleName() + " " + playerContext.getId() + " " + playerContext.getCurrMsgId() + " " + wrapper.getId());
		CrossContextManager.jumpResponse(playerContext, otherPlayer, wrapper.getProtoId(), (GeneratedMessageV3) response);
	}

	@Override
	public void fastResponse() {
		//不需要快速响应
	}
}
