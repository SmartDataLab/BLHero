/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "recharge_one_date", comment = "玩家某一天的充值表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid", columns = { "channel_id" }),
		@Index(name = "channel_player_date", columns = { "channel_id", "player_id", "date_str" }, type = IndexType.UNIQUE) })
public class RechargeOneDate extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名称", readonly = true)
	private String channelName;
	@Column(name = "server_uid", comment = "服务器唯一ID", readonly = true)
	private long serverUid;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(name = "server_name", comment = "服务器名字", readonly = true)
	private String serverName;
	@Column(name = "player_id", comment = "玩家ID", readonly = true)
	private long playerId;
	@Column(comment = "玩家昵称", readonly = true)
	private String nick;
	@Column(name = "date_str", comment = "日期yyyyMMdd", readonly = true)
	private String dateStr;
	@Column(name = "total_pay", comment = "当日总支付")
	private long totalPay;
	@Column(name = "last_recharge_time", comment = "当日最后充值时间")
	private LocalDateTime lastRechargeTime = LocalDateTime.now();
	
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(long totalPay) {
		this.totalPay = totalPay;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
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
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public LocalDateTime getLastRechargeTime() {
		return lastRechargeTime;
	}
	public void setLastRechargeTime(LocalDateTime lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}
}
