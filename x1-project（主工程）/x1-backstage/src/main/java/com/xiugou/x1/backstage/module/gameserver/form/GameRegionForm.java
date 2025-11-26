/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

/**
 * @author YY
 *
 */
public class GameRegionForm {
	private long id;
	private int regionId;
	private String name;
	private long channelId;
	private int serverType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerType() {
		return serverType;
	}
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
}
