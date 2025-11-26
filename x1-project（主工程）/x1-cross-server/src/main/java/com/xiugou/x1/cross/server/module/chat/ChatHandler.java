/**
 * 
 */
package com.xiugou.x1.cross.server.module.chat;

import java.util.Collections;
import java.util.List;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.cross.server.foundation.game.GameContext;
import com.xiugou.x1.cross.server.module.chat.service.ChatingToCrossService;

import pb.xiugou.x1.protobuf.chat.Chat.ChatChannelInfoRequest;
import pb.xiugou.x1.protobuf.chat.Chat.ChatChannelInfoResponse;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatChannel;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;
import pb.xiugou.x1.protobuf.chat.ChatRpc.ChatToRpcRequest;

/**
 * @author yy
 *
 */
@Controller
public class ChatHandler {

	@Autowired
	private ChatingToCrossService chatingToCrossService;
	
	@PlayerCrossCmd
	public ChatChannelInfoResponse channelInfo(GameContext gameContext, long playerId, ChatChannelInfoRequest request) {
		List<PbChatData> chatDatas = null;
		if(request.getChannel() == PbChatChannel.CROSS_CHAT.getNumber()) {
			chatDatas = chatingToCrossService.getChats(playerId, gameContext.getId());
		} else {
			chatDatas = Collections.emptyList();
		}
		
		ChatChannelInfoResponse.Builder response = ChatChannelInfoResponse.newBuilder();
		response.addAllChatDatas(chatDatas);
		response.setChannel(request.getChannel());
		response.setTargetId(request.getTargetId());
		return response.build();
	}
	
	@CrossCmd
	public void chatTo(GameContext gameContext, ChatToRpcRequest request) {
		if(request.getChannel() == PbChatChannel.CROSS_CHAT.getNumber()) {
			chatingToCrossService.chat(gameContext.getId(), request.getChatData(), request.getOriContent(), request.getDoubt());
		}
	}
}
