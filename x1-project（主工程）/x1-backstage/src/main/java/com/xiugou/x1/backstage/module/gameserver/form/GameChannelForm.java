/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

/**
 * @author YY
 *
 */
public class GameChannelForm {
	private long id;
	private String name;
	private long bulletinId;
	private long platformId;
	private String programVersion;
	private String resourceVersion;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBulletinId() {
		return bulletinId;
	}
	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
	}
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public String getProgramVersion() {
		return programVersion;
	}
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}
	public String getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
}
