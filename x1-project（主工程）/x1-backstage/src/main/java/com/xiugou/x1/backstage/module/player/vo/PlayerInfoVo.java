/**
 * 
 */
package com.xiugou.x1.backstage.module.player.vo;

import java.time.LocalDateTime;

/**
 * @author YY
 *
 */
public class PlayerInfoVo {
	private long id;
	private String channelInfo;
	private String serverInfo;
	private long playerId;
	private String openId;
	private String nick;
	private String head;
	private int sex;
	private int level;
	private long gold;
	private long diamond;
	private long fighting;
	//充值金额，单位分
	private long recharge;
	private boolean online;
	//今日在线时长
	private long dailyOnline;
	private LocalDateTime bornTime = LocalDateTime.now();
	private LocalDateTime lastLoginTime = LocalDateTime.now();
	private String quickUrl;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public long getDiamond() {
		return diamond;
	}
	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public long getDailyOnline() {
		return dailyOnline;
	}
	public void setDailyOnline(long dailyOnline) {
		this.dailyOnline = dailyOnline;
	}
	public LocalDateTime getBornTime() {
		return bornTime;
	}
	public void setBornTime(LocalDateTime bornTime) {
		this.bornTime = bornTime;
	}
	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getChannelInfo() {
		return channelInfo;
	}
	public void setChannelInfo(String channelInfo) {
		this.channelInfo = channelInfo;
	}
	public String getServerInfo() {
		return serverInfo;
	}
	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
	public long getRecharge() {
		return recharge;
	}
	public void setRecharge(long recharge) {
		this.recharge = recharge;
	}
	public String getQuickUrl() {
		return quickUrl;
	}
	public void setQuickUrl(String quickUrl) {
		this.quickUrl = quickUrl;
	}
}
