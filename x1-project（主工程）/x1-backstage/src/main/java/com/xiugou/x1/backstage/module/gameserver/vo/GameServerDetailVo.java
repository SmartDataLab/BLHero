/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

import java.time.LocalDateTime;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServerRuntime;

/**
 * @author YY
 *
 */
public class GameServerDetailVo {
	private int id;
	private long platformId;
	private String platformName;
	private int serverId;
	private String name;
	private String socketType;
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
	private boolean hide;
	private int status;
	private boolean recommend;
	private LocalDateTime openTime;
	private LocalDateTime realOpenTime;
	private int sendOpenStatus;
	private boolean opening;
	private int serverType;
	//运行时的参数
	private boolean running;
	private LocalDateTime heartTime;
	private int registerNum;
	private int createNum;
	private int onlineNum;
	private int battleNum;
	private int currBattleNum;
	private String maxMemory;
	private String freeMemory;
	private String totalMemory;
	private String leftMemory;
	private String usedMemory;
	
	public GameServerDetailVo() {
	}
	
	public GameServerDetailVo(GameServer gameServer, GameServerRuntime runtime) {
		this.id = gameServer.getId();
		this.serverId = gameServer.getServerId();
		this.name = gameServer.getName();
		this.platformId = gameServer.getPlatformId();
		this.platformName = gameServer.getPlatformName();
		this.socketType = gameServer.getSocketType();
		this.externalIp = gameServer.getExternalIp();
		this.internalIp = gameServer.getInternalIp();
		this.tcpPort = gameServer.getTcpPort();
		this.httpPort = gameServer.getHttpPort();
		this.dbIp = "";
		this.dbPort = "";
		this.dbUser = "";
		this.dbPwd = "";
		this.dbGameName = gameServer.getDbGameName();
		this.dbLogName = gameServer.getDbLogName();
		this.status = gameServer.getStatus();
		this.recommend = gameServer.isRecommend();
		this.openTime = gameServer.getOpenTime();
		this.realOpenTime = gameServer.getRealOpenTime();
		this.sendOpenStatus = gameServer.getSendOpenStatus();
		this.opening = LocalDateTime.now().isAfter(gameServer.getRealOpenTime());
		this.serverType = gameServer.getServerType();
		this.hide = gameServer.isHide();
		
		if(runtime != null) {
			this.running = runtime.isRunning();
			this.heartTime = runtime.getHeartTime();
			this.registerNum = runtime.getRegisterNum();
			this.createNum = runtime.getCreateNum();
			this.onlineNum = runtime.getOnlineNum();
			this.battleNum = runtime.getBattleNum();
			this.currBattleNum = runtime.getCurrBattleNum();
			this.maxMemory = runtime.getMaxMemory();
			this.freeMemory = runtime.getFreeMemory();
			this.totalMemory = runtime.getTotalMemory();
			this.leftMemory = runtime.getLeftMemory();
			this.usedMemory = runtime.getUsedMemory();
		}
	}
	
	public GameServerDetailVo(GameServer gameServer) {
		this(gameServer, null);
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
	public LocalDateTime getOpenTime() {
		return openTime;
	}
	public void setOpenTime(LocalDateTime openTime) {
		this.openTime = openTime;
	}
	public LocalDateTime getRealOpenTime() {
		return realOpenTime;
	}
	public void setRealOpenTime(LocalDateTime realOpenTime) {
		this.realOpenTime = realOpenTime;
	}
	public boolean isOpening() {
		return opening;
	}

	public void setOpening(boolean opening) {
		this.opening = opening;
	}

	public int getSendOpenStatus() {
		return sendOpenStatus;
	}

	public void setSendOpenStatus(int sendOpenStatus) {
		this.sendOpenStatus = sendOpenStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getServerType() {
		return serverType;
	}

	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	public LocalDateTime getHeartTime() {
		return heartTime;
	}

	public void setHeartTime(LocalDateTime heartTime) {
		this.heartTime = heartTime;
	}

	public int getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}

	public int getCreateNum() {
		return createNum;
	}

	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	public int getOnlineNum() {
		return onlineNum;
	}

	public int getBattleNum() {
		return battleNum;
	}

	public int getCurrBattleNum() {
		return currBattleNum;
	}

	public String getMaxMemory() {
		return maxMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public String getTotalMemory() {
		return totalMemory;
	}

	public String getLeftMemory() {
		return leftMemory;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}

	public void setBattleNum(int battleNum) {
		this.battleNum = battleNum;
	}

	public void setCurrBattleNum(int currBattleNum) {
		this.currBattleNum = currBattleNum;
	}

	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public void setLeftMemory(String leftMemory) {
		this.leftMemory = leftMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
}
