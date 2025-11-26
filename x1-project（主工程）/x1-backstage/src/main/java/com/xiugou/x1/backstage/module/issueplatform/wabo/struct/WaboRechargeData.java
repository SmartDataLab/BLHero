/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.wabo.struct;

/**
 * @author YY
 *
 */
public class WaboRechargeData {
	private String ev;//支付成功
	private String gameId;//gameId
	private String pn;//包名
	private String system;//android ios
	private String gameAccountId;//用户ID
	private String gameOrderId;//订单ID
	private String cpOrderId;//cp自定义的订单ID
	private String extraInfo;//创单时候传入的extraInfo 使用json格式
	private String itemId;//品相ID
	private String itemDoublePrice;//浮点数的价格
	private String itemCurrency;//货币单位
	private String transactionId;//第三方平台的订单ID
	private String payTime;//支付时间戳
	private String payTimeStr;//支付时间戳 字符串
	private String timestamp;
	
	public String getEv() {
		return ev;
	}
	public void setEv(String ev) {
		this.ev = ev;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getGameAccountId() {
		return gameAccountId;
	}
	public void setGameAccountId(String gameAccountId) {
		this.gameAccountId = gameAccountId;
	}
	public String getGameOrderId() {
		return gameOrderId;
	}
	public void setGameOrderId(String gameOrderId) {
		this.gameOrderId = gameOrderId;
	}
	public String getCpOrderId() {
		return cpOrderId;
	}
	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemDoublePrice() {
		return itemDoublePrice;
	}
	public void setItemDoublePrice(String itemDoublePrice) {
		this.itemDoublePrice = itemDoublePrice;
	}
	public String getItemCurrency() {
		return itemCurrency;
	}
	public void setItemCurrency(String itemCurrency) {
		this.itemCurrency = itemCurrency;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayTimeStr() {
		return payTimeStr;
	}
	public void setPayTimeStr(String payTimeStr) {
		this.payTimeStr = payTimeStr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
