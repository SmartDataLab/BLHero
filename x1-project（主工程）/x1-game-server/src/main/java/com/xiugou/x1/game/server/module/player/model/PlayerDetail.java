/**
 * 
 */
package com.xiugou.x1.game.server.module.player.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "player_detail", comment = "玩家杂项数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class PlayerDetail extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "chat_cds", comment = "聊天冷却时间", length = 500)
	private Map<Integer, Long> chatCds = new HashMap<>();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public Map<Integer, Long> getChatCds() {
		return chatCds;
	}

	public void setChatCds(Map<Integer, Long> chatCds) {
		this.chatCds = chatCds;
	}
}
