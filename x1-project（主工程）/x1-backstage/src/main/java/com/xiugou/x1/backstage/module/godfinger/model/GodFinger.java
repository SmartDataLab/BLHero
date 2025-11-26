/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "god_finger", comment = "金手指表", dbAlias = "backstage")
public class GodFinger extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名字", readonly = true)
	private String channelName;
	@Column(name = "server_uid", comment = "服务器唯一ID", readonly = true)
	private int serverUid;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(name = "server_name", comment = "服务器名字", readonly = true)
	private String serverName;
	@Column(name = "player_id", comment = "玩家ID", readonly = true)
	private long playerId;
	@Column(name = "open_id", comment = "账号ID", readonly = true)
	private String openId;
	@Column(comment = "玩家昵称", readonly = true)
	private String nick;
	@Column(comment = "备注")
	private String remark;
	@Column(comment = "每日金额")
	private long money;
	@Column(name = "user_id", comment = "添加金手指的用户", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "添加金手指的用户", readonly = true)
	private String userName;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
