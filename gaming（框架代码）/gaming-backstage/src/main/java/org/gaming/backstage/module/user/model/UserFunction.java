/**
 * 
 */
package org.gaming.backstage.module.user.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "user_function", comment = "用户功能权限表", dbAlias = "backstage", indexs = {
		@Index(name = "unique_uf", columns = { "user_id", "function_id" }, type = IndexType.UNIQUE) })
public class UserFunction extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "user_id", comment = "用户ID")
	private long userId;
	@Column(name = "user_name", comment = "用户名称")
	private String userName;
	@Column(name = "function_id", comment = "功能菜单ID")
	private long functionId;
	@Column(name = "function_name", comment = "功能菜单名字")
	private String functionName;
	@Column(comment = "是否具有写权限")
	private boolean writee;
	@Column(name = "grant_user_id", comment = "授权的用户ID")
	private long grantUserId;
	@Column(name = "grant_user_name", comment = "授权的用户名称")
	private String grantUserName;

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

	public long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(long functionId) {
		this.functionId = functionId;
	}

	public boolean isWritee() {
		return writee;
	}

	public void setWritee(boolean writee) {
		this.writee = writee;
	}

	@Override
	public Long redisOwnerKey() {
		return userId;
	}

	@Override
	public Long redisHashKey() {
		return functionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public long getGrantUserId() {
		return grantUserId;
	}

	public void setGrantUserId(long grantUserId) {
		this.grantUserId = grantUserId;
	}

	public String getGrantUserName() {
		return grantUserName;
	}

	public void setGrantUserName(String grantUserName) {
		this.grantUserName = grantUserName;
	}
}
