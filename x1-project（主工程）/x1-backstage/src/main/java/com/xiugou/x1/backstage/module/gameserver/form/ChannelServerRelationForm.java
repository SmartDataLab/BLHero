/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class ChannelServerRelationForm {
	private long channelId;
	private List<Integer> serverUids = new ArrayList<>();
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public List<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(List<Integer> serverUids) {
		this.serverUids = serverUids;
	}
}
