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
@Table(name = "game_region", comment = "游戏大区表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid_regionid", columns = { "channel_id", "region_id" }, type = IndexType.UNIQUE) })
public class GameRegion extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1)
	@Column(comment = "数据唯一ID")
	private long id;
	@Column(name = "region_id", comment = "大区ID")
	private int regionId;
	@Column(comment = "大区名称")
	private String name;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名字")
	private String channelName;
	@Column(name = "server_type", comment = "服务器类型，1测试服，2审核服，3正式服")
	private int serverType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Long redisOwnerKey() {
		return channelId;
	}
	@Override
	public Long redisHashKey() {
		return (long)regionId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public int getServerType() {
		return serverType;
	}
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
}
