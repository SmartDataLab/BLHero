/**
 * 
 */
package com.xiugou.x1.cross.server.module.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.game.GameContextManager;
import com.xiugou.x1.cross.server.module.chat.model.ChatCross;
import com.xiugou.x1.cross.server.module.server.model.CrossGroup;
import com.xiugou.x1.cross.server.module.server.service.CrossGroupSystemService;

import pb.xiugou.x1.protobuf.chat.Chat.PbChatChannel;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;
import pb.xiugou.x1.protobuf.chat.ChatRpc.ChatToRpcResponse;

/**
 * @author YY
 *
 */
@Service
public class ChatingToCrossService implements IChatCrossService {

	@Autowired
	private CrossGroupSystemService crossGroupSystemService;
	@Autowired
	private ChatCrossService chatCrossService;
	
	private ConcurrentMap<Integer, Queue<PbChatData>> hisGroupChats = new ConcurrentHashMap<>();
	
	@Override
	public List<PbChatData> getChats(long playerId, long targetId) {
		Queue<PbChatData> queue = getChatQueue((int)targetId);
		return new ArrayList<>(queue);
	}

	@Override
	public void chat(long targetId, PbChatData chatData, String oriContent, boolean doubt) {
		int gameServerId = (int)targetId;
		CrossGroup crossGroup = crossGroupSystemService.getCrossGroup(gameServerId);
		
		ChatCross chat = new ChatCross();
		chat.setSpeakerId(chatData.getSpeakerId());
		chat.setSpeakerNick(chatData.getSpeakerNick());
		chat.setSex(chatData.getSex());
		chat.setHead(chatData.getHead());
		chat.setContent(chatData.getContent());
		chat.setOriContent(oriContent);
		chat.setDoubt(doubt);
		chat.setGameServerId(gameServerId);
		chat.setGroupId(crossGroup.getId());
		chat.setLevel(chatData.getLevel());
		chat.setVipLevel(chatData.getVipLevel());
		chatCrossService.insert(chat);
		
		PbChatData pbChat = chat.build();
		Queue<PbChatData> queue = getChatQueue(gameServerId);
		queue.add(pbChat);
		
		ChatToRpcResponse.Builder message = ChatToRpcResponse.newBuilder();
		message.setChannel(PbChatChannel.CROSS_CHAT.getNumber());
		message.setTargetId(targetId);
		message.setChatData(pbChat);
		message.setDoubt(doubt);
		
		if(!chat.isDoubt()) {
			queue.add(pbChat);
			while(queue.size() > 100) {
				queue.poll();
			}
			GameContextManager.writeTo(crossGroup.getServers(), ChatToRpcResponse.Proto.ID, message.build());
		} else {
			//可疑的消息仅推送给本人
			GameContextManager.writeTo(gameServerId, ChatToRpcResponse.Proto.ID, message.build());
		}
	}

	private Queue<PbChatData> getChatQueue(int gameServerId) {
		int crossGroupId = crossGroupSystemService.getCrossGroupId(gameServerId);
		Queue<PbChatData> queue = hisGroupChats.get(crossGroupId);
		if(queue == null) {
			synchronized (this) {
				queue = hisGroupChats.get(crossGroupId);
				if(queue == null) {
					queue = new ConcurrentLinkedQueue<>();
					hisGroupChats.put(crossGroupId, queue);
				}
			}
		}
		return queue;
	}
}
