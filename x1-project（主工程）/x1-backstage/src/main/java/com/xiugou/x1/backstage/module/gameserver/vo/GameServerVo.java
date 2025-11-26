/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;

/**
 * @author YY
 *
 */
public class GameServerVo {
	private long id;
	private String name;
	private String ip;
	private int port;
	private int status;
	private String socketType;
	
	public GameServerVo() {}
	
	public GameServerVo(GameServer gameServer) {
		this.id = gameServer.getServerId();
		this.name = gameServer.getName();
		this.ip = gameServer.getExternalIp();
		this.port = gameServer.getTcpPort();
		this.status = gameServer.getStatus();
		this.socketType = gameServer.getSocketType();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}
}
