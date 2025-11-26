/**
 * 
 */
package com.xiugou.x1.game.server.module.chat;

import java.util.List;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.util.SensitiveUtil;
import org.gaming.ruler.util.SensitiveUtil.SensitiveResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.cross.CrossContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.chat.service.AbstractChatingToChannelService;

import pb.xiugou.x1.protobuf.chat.Chat.ChatChannelInfoRequest;
import pb.xiugou.x1.protobuf.chat.Chat.ChatChannelInfoResponse;
import pb.xiugou.x1.protobuf.chat.Chat.ChatToMessage;
import pb.xiugou.x1.protobuf.chat.Chat.ChatToRequest;
import pb.xiugou.x1.protobuf.chat.Chat.ChatToResponse;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;
import pb.xiugou.x1.protobuf.chat.ChatRpc.ChatToRpcResponse;

/**
 * @author YY
 *
 */
@Controller
public class ChatHandler {

	//TODO 其他渠道
//	@Autowired
//	private DifChannelManager difChannelManager;
	
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@PlayerCmd
	public ChatChannelInfoResponse channelInfo(PlayerContext playerContext, ChatChannelInfoRequest request) {
		AbstractChatingToChannelService chatingService = AbstractChatingToChannelService.getChatingService(request.getChannel());
		Asserts.isTrue(chatingService != null, TipsCode.CHAT_CHANNEL_MISS, request.getChannel());
		List<PbChatData> result = chatingService.getChatDatas(playerContext, request.getTargetId());
		if(result == null) {
			return null;
		}
		
		ChatChannelInfoResponse.Builder response = ChatChannelInfoResponse.newBuilder();
		response.setChannel(request.getChannel());
		response.setTargetId(request.getTargetId());
		response.addAllChatDatas(result);
		response.setChatCd(chatingService.getChatCd(playerContext.getId()));
		return response.build();
	}
	
	@PlayerCrossCmd
	public ChatChannelInfoResponse channelInfo(long playerId, ChatChannelInfoResponse response) {
		AbstractChatingToChannelService chatingService = AbstractChatingToChannelService.getChatingService(response.getChannel());
		
		ChatChannelInfoResponse.Builder builder = response.toBuilder();
		builder.setChatCd(chatingService.getChatCd(playerId));
		return builder.build();
	}
	
	@PlayerCmd
	public ChatToResponse chatTo(PlayerContext playerContext, ChatToRequest request) {
		String sensitiveWord = SensitiveUtil.has(request.getContent());
		Asserts.isTrue(sensitiveWord == null, TipsCode.CHAT_HAS_SENSITIVE);
		
		SensitiveResult sensitiveResult = SensitiveUtil.matchAnyPattern(request.getContent());
		
		AbstractChatingToChannelService chatingService = AbstractChatingToChannelService.getChatingService(request.getChannel());
		Asserts.isTrue(chatingService != null, TipsCode.CHAT_CHANNEL_MISS, request.getChannel());
		chatingService.chatTo(playerContext, request.getTargetId(), request.getContent(), request.getContent(), sensitiveResult.isMatchAnyPattern());
		
		ChatToResponse.Builder response = ChatToResponse.newBuilder();
		response.setChannel(request.getChannel());
		response.setChatCd(chatingService.markChatCd(playerContext.getId()));
		return response.build();
	}
	
	/**
	 * 群聊
	 * @param playerId
	 * @param response
	 */
	@CrossCmd
	public void chatTo(CrossContext crossContext, ChatToRpcResponse response) {
		if(response.getDoubt()) {
			//可疑的消息只推送给发言者本人
			ChatToMessage.Builder pbMessage = ChatToMessage.newBuilder();
			pbMessage.setChannel(response.getChannel());
			pbMessage.setTargetId(response.getTargetId());
			pbMessage.setChatData(response.getChatData());
			playerContextManager.push(response.getChatData().getSpeakerId(), ChatToMessage.Proto.ID, pbMessage.build());
		} else {
			ChatToMessage.Builder pbMessage = ChatToMessage.newBuilder();
			pbMessage.setChannel(response.getChannel());
			pbMessage.setTargetId(response.getTargetId());
			pbMessage.setChatData(response.getChatData());
			playerContextManager.pushAll(ChatToMessage.Proto.ID, pbMessage.build());
		}
	}
}
