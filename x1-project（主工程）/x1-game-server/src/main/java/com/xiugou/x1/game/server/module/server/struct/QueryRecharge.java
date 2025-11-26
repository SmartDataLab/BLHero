/**
 * 
 */
package com.xiugou.x1.game.server.module.server.struct;

/**
 * @author YY
 *
 */
public class QueryRecharge {
	private long playerId;
	private long payMoney;
	private int productId;
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
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
