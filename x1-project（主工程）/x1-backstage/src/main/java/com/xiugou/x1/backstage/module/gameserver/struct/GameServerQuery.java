/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class GameServerQuery extends PageQuery {
	private long platformId;
	private int serverId;
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
