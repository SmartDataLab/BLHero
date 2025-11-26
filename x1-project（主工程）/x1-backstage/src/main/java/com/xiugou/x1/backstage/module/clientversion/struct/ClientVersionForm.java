/**
 * 
 */
package com.xiugou.x1.backstage.module.clientversion.struct;

/**
 * @author YY
 *
 */
public class ClientVersionForm {
	private long id;
	private long channelId;
	private int versionCode;
	private int serverType;
	private String remark;
	private String remoteUrl;
	private String resourceVersion;
	private String pcResourceVersion;
	private String quickUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	public String getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	public int getServerType() {
		return serverType;
	}
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
	public String getPcResourceVersion() {
		return pcResourceVersion;
	}
	public void setPcResourceVersion(String pcResourceVersion) {
		this.pcResourceVersion = pcResourceVersion;
	}
	public String getQuickUrl() {
		return quickUrl;
	}
	public void setQuickUrl(String quickUrl) {
		this.quickUrl = quickUrl;
	}
}
