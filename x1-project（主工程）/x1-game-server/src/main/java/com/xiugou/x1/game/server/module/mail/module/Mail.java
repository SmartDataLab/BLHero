package com.xiugou.x1.game.server.module.mail.module;

import org.gaming.db.annotation.*;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.mail.IMail;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yh
 * @date 2023/5/31
 * @apiNote
 */
@Repository
@JvmCache(relation = {"receiver", "id"})
@Table(name = "mail", comment = "邮件表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "receiver", columns = { "receiver" }) })
public class Mail extends AbstractEntity implements IMail {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "邮件自增ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long receiver;
	@Column(comment = "邮件模板ID", readonly = true)
	private int template;
	@Column(name = "title_args", comment = "标题参数", readonly = true, length = 500)
	private List<String> titleArgs = new ArrayList<>();
	@Column(name = "content_args", comment = "内容参数", readonly = true, length = 5000)
	private List<String> contentArgs = new ArrayList<>();
	@Column(comment = "附件", readonly = true, extra = "text")
	private String attachment;
	@Column(comment = "是否已读")
	private boolean read;
	@Column(comment = "是否已领")
	private boolean receive;
	@Column(name = "expire_time", comment = "到期时间", readonly = true)
	private LocalDateTime expireTime = LocalDateTime.now();
	@Column(name = "game_cause", comment = "游戏事件")
	private int gameCause;
	@Column(name = "game_cause_text", comment = "游戏事件")
	private String gameCauseText;
	@Column(name = "from_pool_id", comment = "来自哪个系统邮件")
	private long fromPoolId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getReceiver() {
		return receiver;
	}
	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}
	public int getTemplate() {
		return template;
	}
	public void setTemplate(int template) {
		this.template = template;
	}
	public List<String> getTitleArgs() {
		return titleArgs;
	}
	public void setTitleArgs(List<String> titleArgs) {
		this.titleArgs = titleArgs;
	}
	public List<String> getContentArgs() {
		return contentArgs;
	}
	public void setContentArgs(List<String> contentArgs) {
		this.contentArgs = contentArgs;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean isReceive() {
		return receive;
	}
	public void setReceive(boolean receive) {
		this.receive = receive;
	}
	public LocalDateTime getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}
	@Override
	public void markUnread() {
		this.read = false;
	}
	@Override
	public void markRead() {
		this.read = true;
	}
	@Override
	public void markUnreceive() {
		this.receive = false;
	}
	@Override
	public void markReceive() {
		this.receive = true;
	}
	public int getGameCause() {
		return gameCause;
	}
	public void setGameCause(int gameCause) {
		this.gameCause = gameCause;
	}
	public String getGameCauseText() {
		return gameCauseText;
	}
	public void setGameCauseText(String gameCauseText) {
		this.gameCauseText = gameCauseText;
	}
	public long getFromPoolId() {
		return fromPoolId;
	}
	public void setFromPoolId(long fromPoolId) {
		this.fromPoolId = fromPoolId;
	}
}
