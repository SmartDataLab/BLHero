package com.xiugou.x1.game.server.module.mail.module;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/6/1
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "mail_player", comment = "玩家邮件表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class MailPlayer extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
    private long pid;
	@Column(name = "read_time", comment = "最后读取系统邮件的时间")
    private LocalDateTime readTime = LocalDateTime.now();
	@Column(name = "last_read_time", comment = "最后读取系统邮件的时间")
	private int lastReadTime;
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public LocalDateTime getReadTime() {
		return readTime;
	}
	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}
	public int getLastReadTime() {
		return lastReadTime;
	}
	public void setLastReadTime(int lastReadTime) {
		this.lastReadTime = lastReadTime;
	}
}
