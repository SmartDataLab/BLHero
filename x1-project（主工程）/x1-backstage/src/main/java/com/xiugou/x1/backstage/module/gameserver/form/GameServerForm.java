/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

/**
 * @author YY
 *
 */
public class GameServerForm {
	//数据唯一ID
	private long id;
	private long platformId;
	private String platformName;
	private int serverId;
	private String name;
	private String externalIp;
	private String internalIp;
	private int tcpPort;
	private int httpPort;
	private String dbIp;
	private String dbPort;
	private String dbUser;
	private String dbPwd;
	private String dbGameName;
	private String dbLogName;
	private int status;
	private boolean recommend;
	private int serverType;
	private String socketType;
	private boolean hide;
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
	public String getDbIp() {
		return dbIp;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
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
	public boolean isRecommend() {
		return recommend;
	}
	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
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
