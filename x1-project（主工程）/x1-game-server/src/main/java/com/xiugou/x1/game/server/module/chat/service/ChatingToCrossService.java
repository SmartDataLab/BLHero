/**
 * 
 */
package com.xiugou.x1.game.server.module.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.cross.CrossContextManager;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.player.model.Player;

import pb.xiugou.x1.protobuf.chat.Chat.ChatChannelInfoRequest;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatChannel;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;
import pb.xiugou.x1.protobuf.chat.ChatRpc.ChatToRpcRequest;

/**
 * @author YY
 *
 */
@Service
public class ChatingToCrossService extends AbstractChatingToChannelService {
	
	@Override
	public PbChatChannel chatChannel() {
		return PbChatChannel.CROSS_CHAT;
	}

	@Override
	public List<PbChatData> getChatDatas(PlayerContext playerContext, long targetId) {
		ChatChannelInfoRequest.Builder request = ChatChannelInfoRequest.newBuilder();
		request.setChannel(chatChannel().getNumber());
		CrossContextManager.write(playerContext, ChatChannelInfoRequest.Proto.ID, request.build());
		return null;
	}

	@Override
	public void chatTo(PlayerContext playerContext, long targetId, String content, String oriContent, boolean doubt) {
		Player player = playerService.getEntity(playerContext.getId());
		
		ChatToRpcRequest.Builder rpcRequest = ChatToRpcRequest.newBuilder();
		rpcRequest.setChannel(chatChannel().getNumber());
		rpcRequest.setTargetId(0);
		rpcRequest.setOriContent(oriContent);
		rpcRequest.setDoubt(doubt);
		
		PbChatData.Builder chatData = PbChatData.newBuilder();
		chatData.setSpeakerId(player.getId());
		chatData.setSpeakerNick(player.getNick());
		chatData.setHead(player.getHead());
		chatData.setSex(player.getSex());
		chatData.setContent(content);
		chatData.setLevel(player.getLevel());
		chatData.setVipLevel(player.getVipLevel());
		rpcRequest.setChatData(chatData.build());
		CrossContextManager.write(ChatToRpcRequest.Proto.ID, rpcRequest.build());
	}
}
