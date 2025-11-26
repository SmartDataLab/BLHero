/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.model;

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
@Table(name = "fix_code_result", comment = "热更代码结果记录表", dbAlias = "backstage")
public class FixCodeResult extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "fix_id", comment = "修正记录ID", readonly = true)
	private long fixId;
	@Column(name = "server_uid", comment = "服务器唯一ID", readonly = true)
	private int serverUid;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(comment = "服务器名字", readonly = true)
	private String name;
	@Column(comment = "处理状态，0未处理，1成功，2失败")
	private int status;
	@Column(comment = "服务器响应码")
	private int code;
	@Column(comment = "服务器响应消息")
	private String message;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getFixId() {
		return fixId;
	}
	public void setFixId(long fixId) {
		this.fixId = fixId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
