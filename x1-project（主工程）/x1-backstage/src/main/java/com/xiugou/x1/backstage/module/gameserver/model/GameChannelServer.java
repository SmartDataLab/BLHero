/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "game_channel_server", comment = "游戏渠道与服务器关系表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid", columns = { "channel_id" }),
		@Index(name = "channelid_serverid", columns = { "channel_id", "server_id" }, type = IndexType.UNIQUE)})
public class GameChannelServer extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1)
	@Column(comment = "关系ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名字")
	private String channelName;
	@Column(name = "server_uid", comment = "服务器唯一ID", readonly = true)
	private int serverUid;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(name = "server_name", comment = "服务器名字")
	private String serverName;
	@Column(name = "region_id", comment = "所属大区ID")
	private int regionId;
	@Column(name = "region_name", comment = "所属大区名称")
	private String regionName;
	@Column(name = "user_id", comment = "操作员ID", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "操作员名称", readonly = true)
	private String userName;
	@Column(comment = "是否为推荐服务器")
	private boolean recommend;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	@Override
	public Long redisOwnerKey() {
		return channelId;
	}
	@Override
	public Long redisHashKey() {
		return (long)serverId;
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
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public boolean isRecommend() {
		return recommend;
	}
	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}
}
