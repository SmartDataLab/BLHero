/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "game_channel", comment = "游戏渠道表", dbAlias = "backstage")
public class GameChannel extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1)
	@Column(comment = "渠道ID")
	private long id;
	@Column(comment = "渠道名字")
	private String name;
	@Column(name = "platform_id", comment = "归属平台ID", readonly = true)
	private long platformId;
	@Column(name = "platform_name", comment = "归属平台名称")
	private String platformName;
	@Column(name = "user_id", comment = "操作员ID", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "操作员名称", readonly = true)
	private String userName;
	@Column(name = "bulletin_id", comment = "公告ID")
	private long bulletinId;
	@Column(name = "program_version", comment = "客户端程序版本号")
	private String programVersion;
	@Column(name = "resource_version", comment = "客户端资源版本号")
	private String resourceVersion;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return id;
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
	public long getBulletinId() {
		return bulletinId;
	}
	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
	}
	public String getProgramVersion() {
		return programVersion;
	}
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}
	public String getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
}
