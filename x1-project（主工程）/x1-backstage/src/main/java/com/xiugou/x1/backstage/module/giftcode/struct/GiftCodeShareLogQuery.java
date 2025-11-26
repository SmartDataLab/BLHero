/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class GiftCodeShareLogQuery extends PageQuery {
	private String code;
	private long playerId;
	private String playerName;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
