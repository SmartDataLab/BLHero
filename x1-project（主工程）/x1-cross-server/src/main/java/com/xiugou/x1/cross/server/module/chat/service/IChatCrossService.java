/**
 * 
 */
package com.xiugou.x1.cross.server.module.chat.service;

import java.util.List;

import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;

/**
 * @author YY
 * 跨服聊天接口
 */
public interface IChatCrossService {
	/**
	 * 获取公会频道聊天的信息
	 */
	List<PbChatData> getChats(long playerId, long targetId);
	/**
	 * 聊天
	 */
	void chat(long targetId, PbChatData chatData, String oriContent, boolean doubt);
}
