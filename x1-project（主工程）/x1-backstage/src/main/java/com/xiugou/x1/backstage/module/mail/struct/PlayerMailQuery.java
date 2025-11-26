/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class PlayerMailQuery extends PageQuery {
	private long channleId;
	private long playerId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getChannleId() {
		return channleId;
	}

	public void setChannleId(long channleId) {
		this.channleId = channleId;
	}
}
