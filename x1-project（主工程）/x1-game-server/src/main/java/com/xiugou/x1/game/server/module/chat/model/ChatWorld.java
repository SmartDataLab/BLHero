/**
 * 
 */
package com.xiugou.x1.game.server.module.chat.model;

import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.springframework.stereotype.Repository;

import pojo.xiugou.x1.pojo.module.chat.ChatEntity;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "chat_world", comment = "本服聊天表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ChatWorld extends ChatEntity {

}
