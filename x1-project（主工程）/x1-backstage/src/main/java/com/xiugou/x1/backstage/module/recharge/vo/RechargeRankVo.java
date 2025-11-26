/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.vo;

import java.time.LocalDateTime;

/**
 * @author YY
 *
 */
public class RechargeRankVo {
	private long channelId;
	private String channelName;
	private long serverUid;
	private int serverId;
	private String serverName;
	private long playerId;
	private String nick;
	private int level;
	private long fighting;
	private long diamond;
	private long gold;
	private long money;
	private LocalDateTime lastLoginTime;
	private LocalDateTime bornTime;
	private LocalDateTime lastRechargeTime;
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
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getDiamond() {
		return diamond;
	}
	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public LocalDateTime getBornTime() {
		return bornTime;
	}
	public void setBornTime(LocalDateTime bornTime) {
		this.bornTime = bornTime;
	}
	public LocalDateTime getLastRechargeTime() {
		return lastRechargeTime;
	}
	public void setLastRechargeTime(LocalDateTime lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
}
