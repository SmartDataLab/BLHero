/**
 * 
 */
package com.xiugou.x1.backstage.module.clientlog.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "client_log", comment = "客户端日志", dbAlias = "backstage")
public class ClientLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1)
	@Column(comment = "日志ID")
	private long id;
	@Column(comment = "日志级别")
	private String level;
	@Column(comment = "标题、类别")
	private String title;
	@Column(comment = "日志信息", extra = "text")
	private String content;
	@Column(name = "player_id", comment = "玩家ID")
	private String playerId;
	@Column(comment = "玩家名称")
	private String name;
	@Column(comment = "日志内容", extra = "text")
	private String data;
	@Column(comment = "是否已发送给管理员")
	private boolean send;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSend() {
		return send;
	}
	public void setSend(boolean send) {
		this.send = send;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
