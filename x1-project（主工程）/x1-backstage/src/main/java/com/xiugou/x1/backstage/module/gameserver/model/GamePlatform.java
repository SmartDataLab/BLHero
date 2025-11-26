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
@Table(name = "game_platform", comment = "游戏平台表", dbAlias = "backstage")
public class GamePlatform extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1000)
	@Column(comment = "平台ID")
	private long id;
	@Column(comment = "平台名称")
	private String name;
	@Column(name = "user_id", comment = "操作员ID", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "操作员名称", readonly = true)
	private String userName;
	
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return id;
	}
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
}
