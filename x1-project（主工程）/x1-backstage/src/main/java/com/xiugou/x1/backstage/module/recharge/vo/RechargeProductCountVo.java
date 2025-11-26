/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.vo;

/**
 * @author YY
 *
 */
public class RechargeProductCountVo {
	private int productId;
	private String productName;
	private int playerCount;
	private int buyCount;
	private long totalPay;
	private int rate;	//占比
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public long getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(long totalPay) {
		this.totalPay = totalPay;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}
