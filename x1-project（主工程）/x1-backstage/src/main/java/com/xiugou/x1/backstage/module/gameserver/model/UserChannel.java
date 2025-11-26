/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

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
@Table(name = "user_channel", comment = "用户渠道表", dbAlias = "backstage", indexs = {
		@Index(name = "userId_channelId", columns = { "user_id", "channel_id" }, type = IndexType.UNIQUE) })
public class UserChannel extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "user_id", comment = "用户ID")
	private long userId;
	@Column(name = "user_name", comment = "用户名称")
	private String userName;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名称")
	private String channelName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	@Override
	public Long redisOwnerKey() {
		return userId;
	}

	@Override
	public Long redisHashKey() {
		return channelId;
	}
}
