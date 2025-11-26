/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

import java.util.List;

/**
 * @author YY
 *
 */
public class GameServerMaintainForm {
	private long platformId;
	private List<Integer> serverIds;
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public List<Integer> getServerIds() {
		return serverIds;
	}
	public void setServerIds(List<Integer> serverIds) {
		this.serverIds = serverIds;
	}
}
