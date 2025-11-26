/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class RechargeVirtualQuery extends PageQuery {
	private String openId;
	private long playerId;
	private String nick;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
