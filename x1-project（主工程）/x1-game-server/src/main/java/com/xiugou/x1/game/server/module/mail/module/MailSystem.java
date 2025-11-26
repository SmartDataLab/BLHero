package com.xiugou.x1.game.server.module.mail.module;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.mail.IMailPool;
import org.gaming.prefab.mail.MailBelong;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/5/31
 * @apiNote
 */
@Repository
@JvmCache(cacheTime = 0, loadAllOnStart = true)
@Table(name = "mail_system", comment = "系统邮件表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class MailSystem extends AbstractEntity implements IMailPool {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "邮件自增ID")
	private long id;
	@Column(comment = "邮件模板ID", readonly = true)
	private int template;
	@Column(comment = "邮件类型 1全服邮件 2特定玩家邮件", readonly = true)
	private MailBelong belong;
	@Column(comment = "收到邮件的玩家们ID", readonly = true, extra = "text")
	private Set<Long> receivers = new HashSet<>();
	@Column(name = "server_ids", comment = "对哪些服务器的玩家开放", readonly = true, length = 1000)
	private Set<Integer> serverIds = new HashSet<>();
	@Column(name = "title_args", comment = "标题参数", readonly = true, length = 2500)
	private List<String> titleArgs = new ArrayList<>();
	@Column(name = "content_args", comment = "内容参数", readonly = true, length = 2500)
	private List<String> contentArgs = new ArrayList<>();
	@Column(comment = "附件", readonly = true, length = 2500)
	private String attachment;
	@Column(name = "expire_time", comment = "到期时间", readonly = true)
	private LocalDateTime expireTime = LocalDateTime.now();
	@Column(name = "game_cause", comment = "游戏事件")
	private int gameCause;
	@Column(name = "game_cause_text", comment = "游戏事件")
	private String gameCauseText;
	@Column(name = "from_id", comment = "后台邮件ID", readonly = true)
	private long fromId;
	@Column(comment = "系统邮件能接受的玩家等级限制", readonly = true)
	private int level;
	@Column(name = "post_time", comment = "发送时间")
	private int postTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getTemplate() {
		return template;
	}
	public void setTemplate(int template) {
		this.template = template;
	}
	public MailBelong getBelong() {
		return belong;
	}
	public void setBelong(MailBelong belong) {
		this.belong = belong;
	}
	public Set<Long> getReceivers() {
		return receivers;
	}
	public void setReceivers(Set<Long> receivers) {
		this.receivers = receivers;
	}
	public Set<Integer> getServerIds() {
		return serverIds;
	}
	public void setServerIds(Set<Integer> serverIds) {
		this.serverIds = serverIds;
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
	public LocalDateTime getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
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
	public long getFromId() {
		return fromId;
	}
	public void setFromId(long fromId) {
		this.fromId = fromId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPostTime() {
		return postTime;
	}
	public void setPostTime(int postTime) {
		this.postTime = postTime;
	}
}
