/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class PlayerQuery extends PageQuery {
	private long playerId;
	private String name;
	private String openId;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
