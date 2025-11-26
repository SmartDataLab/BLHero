/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

import com.xiugou.x1.backstage.module.gameserver.model.GameDb;

/**
 * @author YY
 *
 */
public class GameDbVo {
	private int id;
	private String alias;
	private String ipPort;
	private String dbName;
	private String user;
	private String password;
	private int minIdle;
	private int maxActive;
	private long maxWaitMillis;
	
	public GameDbVo() {}
	public GameDbVo(GameDb gameDb) {
		this.id = gameDb.getZoneId();
		this.alias = gameDb.getAlias();
		this.ipPort = gameDb.getIpPort();
		this.dbName = gameDb.getDbName();
		this.user = gameDb.getUser();
		this.password = gameDb.getPassword();
		this.minIdle = gameDb.getMinIdle();
		this.maxActive = gameDb.getMaxActive();
		this.maxWaitMillis = gameDb.getMaxWaitMillis();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
}
