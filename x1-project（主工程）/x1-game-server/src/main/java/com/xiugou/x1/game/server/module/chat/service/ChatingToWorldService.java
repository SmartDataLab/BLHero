/**
 * 
 */
package com.xiugou.x1.game.server.module.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.chat.model.ChatWorld;
import com.xiugou.x1.game.server.module.player.model.Player;

import pb.xiugou.x1.protobuf.chat.Chat.ChatToMessage;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatChannel;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;

/**
 * @author YY
 *
 */
@Service
public class ChatingToWorldService extends AbstractChatingToChannelService {
	
	@Autowired
	private ChatWorldService chatWorldService;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	private Queue<PbChatData> hisChatDatas = new ConcurrentLinkedQueue<>();
	
	@Override
	public PbChatChannel chatChannel() {
		return PbChatChannel.WORLD_CHAT;
	}

	@Override
	public List<PbChatData> getChatDatas(PlayerContext playerContext, long targetId) {
		return new ArrayList<>(hisChatDatas);
	}

	@Override
	public void chatTo(PlayerContext playerContext, long targetId, String content, String oriContent, boolean doubt) {
		Player player = playerService.getEntity(playerContext.getId());

		ChatWorld chat = new ChatWorld();
		chat.setSpeakerId(player.getId());
		chat.setSpeakerNick(player.getNick());
		chat.setSex(player.getSex());
		chat.setHead(player.getHead());
		chat.setContent(content);
		chat.setOriContent(oriContent);
		chat.setDoubt(doubt);
		chat.setLevel(player.getLevel());
		chat.setVipLevel(player.getVipLevel());
		chatWorldService.insert(chat);
		
		PbChatData pbChat = chat.build();
		ChatToMessage.Builder message = ChatToMessage.newBuilder();
		message.setChannel(chatChannel().getNumber());
		message.setTargetId(0);
		message.setChatData(pbChat);
		if(!chat.isDoubt()) {
			hisChatDatas.add(pbChat);
			while(hisChatDatas.size() > 100) {
				hisChatDatas.poll();
			}
			playerContextManager.pushAll(ChatToMessage.Proto.ID, message.build());
		} else {
			//可疑的消息仅推送给本人
			playerContextManager.push(playerContext.getId(), ChatToMessage.Proto.ID, message.build());	
		}
	}
}
