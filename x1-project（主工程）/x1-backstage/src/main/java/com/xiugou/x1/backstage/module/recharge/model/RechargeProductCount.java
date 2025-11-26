/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.model;

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
@Table(name = "recharge_product_count", comment = "充值商品统计表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid", columns = { "channel_id" }) })
public class RechargeProductCount extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
	@Column(name = "product_id", comment = "商品ID")
	private int productId;
	@Column(name = "product_name", comment = "商品名字")
	private String productName;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(name = "pay_money", comment = "支付金额")
	private long payMoney;
	@Column(name = "callback_id", comment = "回调ID")
	private long callbackId;
	@Column(name = "server_uid", comment = "服务器唯一ID")
	private int serverUid;
	
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}
	public long getCallbackId() {
		return callbackId;
	}
	public void setCallbackId(long callbackId) {
		this.callbackId = callbackId;
	}
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
}
