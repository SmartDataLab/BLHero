/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.form;

/**
 * @author YY
 *
 */
public class RechargeVirtualForm {
	private long playerId;
	private int productId;
	private String remark;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
