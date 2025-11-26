/**
 *
 */
package com.xiugou.x1.backstage.module.player.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "player", comment = "玩家信息表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid_openid", columns = { "channel_id", "open_id" }) })
public class Player extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID，全平台唯一")
	private long id;
	@Column(name = "platform_id", comment = "平台ID")
	private long platformId;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
	@Column(name = "open_id", comment = "账户ID")
	private String openId;
	@Column(name = "server_id", comment = "服务器ID")
	private int serverId;
	@Column(comment = "昵称")
	private String nick;
	@Column(comment = "头像")
	private String head;
	@Column(comment = "性别，0无性别，1男，2女")
	private int sex;
	@Column(comment = "等级")
	private int level;
	@Column(comment = "金币")
	private long gold;
	@Column(comment = "钻石")
	private long diamond;
	@Column(comment = "战力")
	private long fighting;
	@Column(comment = "充值金额")
	private long recharge;
	@Column(comment = "是否在线")
	private boolean online;
	@Column(name = "daily_online", comment = "今日在线时长")
	private long dailyOnline;
	@Column(name = "born_time", comment = "创号时间", readonly = true)
	private LocalDateTime bornTime = LocalDateTime.now();
	@Column(name = "last_login_time", comment = "最后登录时间")
	private LocalDateTime lastLoginTime = LocalDateTime.now();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
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

	public long getDailyOnline() {
		return dailyOnline;
	}

	public void setDailyOnline(long dailyOnline) {
		this.dailyOnline = dailyOnline;
	}

	public long getFighting() {
		return fighting;
	}

	public void setFighting(long fighting) {
		this.fighting = fighting;
	}

	public long getRecharge() {
		return recharge;
	}

	public void setRecharge(long recharge) {
		this.recharge = recharge;
	}

	public long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
}
