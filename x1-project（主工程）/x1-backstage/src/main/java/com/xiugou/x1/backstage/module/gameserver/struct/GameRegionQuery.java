/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class GameRegionQuery extends PageQuery {
	private long channelId;

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
}
