/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.model;

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
@Table(name = "recharge_virtual", comment = "虚拟充值（内部充值）表", dbAlias = "backstage")
public class RechargeVirtual extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
	@Column(name = "server_uid", comment = "服务器唯一ID")
	private long serverUid;
	@Column(name = "server_id", comment = "服务器ID")
	private int serverId;
	@Column(name = "server_name", comment = "服务器名字")
	private String serverName;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(comment = "玩家名字")
	private String nick;
	@Column(name = "open_id", comment = "账号ID")
	private String openId;
	@Column(name = "product_id", comment = "充值商品ID")
	private int productId;
	@Column(name = "product_name", comment = "充值商品名字")
	private String productName;
	@Column(comment = "金额")
	private long money;
	@Column(name = "user_id", comment = "操作员ID")
	private long userId;
	@Column(name = "user_name", comment = "操作员名称")
	private String userName;
	@Column(comment = "充值状态，0未处理，1成功，2失败")
	private int status;
	@Column(comment = "备注")
	private String remark;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
