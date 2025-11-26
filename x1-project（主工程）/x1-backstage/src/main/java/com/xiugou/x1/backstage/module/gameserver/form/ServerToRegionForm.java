/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

/**
 * @author YY
 *
 */
public class ServerToRegionForm {
	private long channelId;
	private long serverUid;
	private long regionId;
	public long getRegionId() {
		return regionId;
	}
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
	}
}
