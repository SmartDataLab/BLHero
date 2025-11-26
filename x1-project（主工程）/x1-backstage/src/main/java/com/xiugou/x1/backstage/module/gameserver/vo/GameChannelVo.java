/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

/**
 * @author YY
 *
 */
public class GameChannelVo {
	private long id;
	private String name;
	private String platformInfo;
	private long bulletinId;
	private String programVersion;
	private String resourceVersion;
	private long userId;
	private String userName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlatformInfo() {
		return platformInfo;
	}
	public void setPlatformInfo(String platformInfo) {
		this.platformInfo = platformInfo;
	}
	public long getBulletinId() {
		return bulletinId;
	}
	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
