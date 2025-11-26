/**
 * 
 */
package org.gaming.backstage.module.user.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "user_log", comment = "用户操作日志表", dbAlias = "backlog", asyncType = AsyncType.INSERT, byColumn = "time", indexs = {
		@Index(name = "user_id", columns = { "user_id" }) })
public class UserLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "user_id", comment = "用户ID")
	private long userId;
	@Column(name = "user_name", comment = "用户名称")
	private String userName;
	@Column(name = "request_url", comment = "请求地址")
	private String requestUrl;
	@Column(comment = "操作参数", extra = "text")
	private String param;
	@Column(comment = "操作时间")
	private LocalDateTime time = LocalDateTime.now();
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
}
