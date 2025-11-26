/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "recharge_callback", comment = "充值回调表", dbAlias = "backstage")
public class RechargeCallback extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "platform_id", comment = "平台ID", readonly = true)
	private long platformId;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(name = "player_id", comment = "玩家ID", readonly = true)
	private long playerId;
	@Column(comment = "玩家名字", readonly = true)
	private String nick;
	@Column(name = "open_id", comment = "账号ID", readonly = true)
	private String openId;
	@Column(comment = "下单等级", readonly = true)
	private int level;
	@Column(name = "sdk_order_id", comment = "渠道订单ID", readonly = true)
	private String sdkOrderId;
	@Column(name = "game_order_id", comment = "游戏服的订单ID", readonly = true)
	private String gameOrderId;
	@Column(comment = "支付金额", readonly = true)
	private long money;
	@Column(name = "product_id", comment = "充值商品ID", readonly = true)
	private int productId;
	@Column(name = "product_name", comment = "充值商品名字", readonly = true)
	private String productName;
	@Column(comment = "发货状态，0未处理，1发货成功，2发货失败")
	private int give;
	@Column(comment = "备注", length = 3000)
	private String remark;
	@Column(name = "remark_idx", comment = "备注序号")
	private int remarkIdx;
	@Column(comment = "是否测试订单，1是，0不是")
	private int test;
	@Column(name = "all_check", comment = "是否所有验证都通过")
	private boolean allCheck;
	@Column(comment = "订单签名")
	private String sign;
	@Column(name = "local_sign", comment = "本地订单签名")
	private String localSign;
	@Column(name = "extra_sign", comment = "扩展参数签名")
	private String extraSign;
	@Column(name = "local_extra_sign", comment = "本地扩展参数签名")
	private String localExtraSign;
	@Column(name = "remote_ip", comment = "请求的IP")
	private String remoteIp;
	@Column(name = "callback_data", comment = "回调原数据", extra = "text", readonly = true)
	private String callbackData;
	@Column(name = "game_response", comment = "游戏服的响应")
	private String gameResponse;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCallbackData() {
		return callbackData;
	}
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}
	public String getSdkOrderId() {
		return sdkOrderId;
	}
	public void setSdkOrderId(String sdkOrderId) {
		this.sdkOrderId = sdkOrderId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
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
	public String getGameResponse() {
		return gameResponse;
	}
	public void setGameResponse(String gameResponse) {
		this.gameResponse = gameResponse;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getGive() {
		return give;
	}
	public void setGive(int give) {
		this.give = give;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remarkIdx += 1;
		if(this.remark == null) {
			this.remark = "";
		}
		this.remark = this.remark + this.remarkIdx + "." + remark + ";";
	}
	public int getTest() {
		return test;
	}
	public void setTest(int test) {
		this.test = test;
	}
	public int getRemarkIdx() {
		return remarkIdx;
	}
	public void setRemarkIdx(int remarkIdx) {
		this.remarkIdx = remarkIdx;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getLocalSign() {
		return localSign;
	}
	public void setLocalSign(String localSign) {
		this.localSign = localSign;
	}
	public String getExtraSign() {
		return extraSign;
	}
	public void setExtraSign(String extraSign) {
		this.extraSign = extraSign;
	}
	public String getLocalExtraSign() {
		return localExtraSign;
	}
	public void setLocalExtraSign(String localExtraSign) {
		this.localExtraSign = localExtraSign;
	}
	public boolean isAllCheck() {
		return allCheck;
	}
	public void setAllCheck(boolean allCheck) {
		this.allCheck = allCheck;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getGameOrderId() {
		return gameOrderId;
	}
	public void setGameOrderId(String gameOrderId) {
		this.gameOrderId = gameOrderId;
	}
	
}
