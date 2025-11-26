/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger.form;

/**
 * @author YY
 *
 */
public class GodFingerForm {
	private long id;
	private long playerId;
	private String remark;
	private long money;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
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
}
