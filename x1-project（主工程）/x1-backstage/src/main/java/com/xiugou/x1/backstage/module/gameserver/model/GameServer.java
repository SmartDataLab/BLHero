/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import java.time.LocalDateTime;

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
@Table(name = "game_server", comment = "游戏服务器表", dbAlias = "backstage", indexs = {
		@Index(name = "platformid_serverid", columns = { "platform_id", "server_id" }, type = IndexType.UNIQUE) })
public class GameServer extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 10000)
	@Column(comment = "数据唯一ID")
	private int id;
	@Column(name = "platform_id", comment = "归属平台ID", readonly = true)
	private long platformId;
	@Column(name = "platform_name", comment = "归属平台名称")
	private String platformName;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(comment = "服务器名字")
	private String name;
	@Column(name = "server_type", comment = "服务器类型，1测试服，2审核服，3正式服")
	private int serverType;
	@Column(name = "socket_type", comment = "socket连接方式")
	private String socketType;
	@Column(name = "external_ip", comment = "外网IP")
	private String externalIp;
	@Column(name = "internal_ip", comment = "内网IP")
	private String internalIp;
	@Column(name = "tcp_port", comment = "tcp/ws端口")
	private int tcpPort;
	@Column(name = "http_port", comment = "http端口")
	private int httpPort;
	@Column(name = "db_game_name", comment = "游戏数据库名")
	private String dbGameName;
	@Column(name = "db_log_name", comment = "日志数据库名")
	private String dbLogName;
	@Column(comment = "状态，0维护，1流畅，2爆满，3新服")
	private int status;
	@Column(comment = "是否为推荐服务器")
	private boolean recommend;
	@Column(name = "open_time", comment = "预期开服时间")
	private LocalDateTime openTime = LocalDateTime.now();
	@Column(name = "real_open_time", comment = "实际开服时间")
	private LocalDateTime realOpenTime = LocalDateTime.now();
	@Column(name = "send_open_status", comment = "开服时间是否发送成功，0未发送，1成功，2失败")
	private int sendOpenStatus;
	@Column(comment = "服务器是否维护中")
	private boolean maintain;
	@Column(name = "maintain_response", comment = "服务器维护消息是否发送成功")
	private boolean maintainResponse;
	@Column(comment = "服务器是否已被合服")
	private boolean merge;
	@Column(comment = "是否隐藏入口")
	private boolean hide;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getDbGameName() {
		return dbGameName;
	}

	public void setDbGameName(String dbGameName) {
		this.dbGameName = dbGameName;
	}

	public String getDbLogName() {
		return dbLogName;
	}

	public void setDbLogName(String dbLogName) {
		this.dbLogName = dbLogName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LocalDateTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalDateTime openTime) {
		this.openTime = openTime;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	@Override
	public Long redisOwnerKey() {
		return 0L;
	}

	@Override
	public Long redisHashKey() {
		return (long) id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getRealOpenTime() {
		return realOpenTime;
	}

	public void setRealOpenTime(LocalDateTime realOpenTime) {
		this.realOpenTime = realOpenTime;
	}

	public int getSendOpenStatus() {
		return sendOpenStatus;
	}

	public void setSendOpenStatus(int sendOpenStatus) {
		this.sendOpenStatus = sendOpenStatus;
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

	public boolean isMaintain() {
		return maintain;
	}

	public void setMaintain(boolean maintain) {
		this.maintain = maintain;
	}

	public boolean isMaintainResponse() {
		return maintainResponse;
	}

	public void setMaintainResponse(boolean maintainResponse) {
		this.maintainResponse = maintainResponse;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
}
