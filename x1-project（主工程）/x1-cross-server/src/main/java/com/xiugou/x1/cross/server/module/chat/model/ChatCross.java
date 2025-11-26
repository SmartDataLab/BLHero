/**
 * 
 */
package com.xiugou.x1.cross.server.module.chat.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.springframework.stereotype.Repository;

import pojo.xiugou.x1.pojo.module.chat.ChatEntity;

/**
 * @author yy
 *
 */
@Repository
@Table(name = "chat_cross", comment = "跨服聊天信息表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ChatCross extends ChatEntity {
	@Column(name = "group_id", comment = "跨服分组ID")
	private int groupId;
	@Column(name = "game_server_id", comment = "服务器ID")
	private int gameServerId;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getGameServerId() {
		return gameServerId;
	}
	public void setGameServerId(int gameServerId) {
		this.gameServerId = gameServerId;
	}
}
