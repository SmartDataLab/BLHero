/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "game_db", comment = "游戏数据库管理表", dbAlias = "backstage", indexs = {
		@Index(name = "db_name", columns = "db_name", type = IndexType.UNIQUE)})
public class GameDb extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(name = "db_name", comment = "数据库名字，唯一ID")
	private String dbName;
	@Column(name = "platform_id", comment = "平台ID")
	private int platformId;
	@Column(name = "server_id", comment = "服务器ID")
	private int serverId;
	@Column(comment = "服务器类型")
	private String type;
	@Column(name = "zone_id", comment = "在服务器里面用的一个ID，基本是1")
	private int zoneId;
	@Column(comment = "别名")
	private String alias;
	@Column(name = "ip_port", comment = "IP与端口，填内网IP与端口")
	private String ipPort;
	@Column(comment = "数据库用户")
	private String user;
	@Column(comment = "数据库密码")
	private String password;
	@Column(name = "min_idle", comment = "最少空闲连接数")
	private int minIdle;
	@Column(name = "max_active", comment = "最大活跃连接数")
	private int maxActive;
	@Column(name = "max_wait_millis", comment = "最大等级时间")
	private int maxWaitMillis;
	
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getIpPort() {
		return ipPort;
	}
	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public int getPlatformId() {
		return platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
