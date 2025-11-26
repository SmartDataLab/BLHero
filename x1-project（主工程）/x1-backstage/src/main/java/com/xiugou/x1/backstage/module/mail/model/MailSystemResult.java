/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "mail_system_result", comment = "系统邮件发送回执表", dbAlias = "backstage")
public class MailSystemResult extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "日志ID")
	private long id;
	@Column(name = "mail_id", comment = "系统邮件ID")
	private long mailId;
	@Column(name = "server_uid", comment = "服务器ID")
	private long serverUid;
	@Column(name = "server_id", comment = "服务器名字")
	private int serverId;
	@Column(name = "server_name", comment = "服务器名字")
	private String serverName;
	@Column(name = "type_text", comment = "结果类型")
	private String typeText;
	@Column(comment = "处理状态，0未处理，1成功，2失败")
	private int status;
	@Column(name = "message", comment = "处理信息")
	private String message;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMailId() {
		return mailId;
	}
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getTypeText() {
		return typeText;
	}
	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}
}
