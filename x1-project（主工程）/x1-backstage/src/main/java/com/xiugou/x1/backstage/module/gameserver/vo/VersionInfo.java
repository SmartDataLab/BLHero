/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

/**
 * @author YY
 *
 */
public class VersionInfo {
	private String remoteUrl;
	private String resourceVersion;
	private String pcResourceVersion;
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
	public String getPcResourceVersion() {
		return pcResourceVersion;
	}
	public void setPcResourceVersion(String pcResourceVersion) {
		this.pcResourceVersion = pcResourceVersion;
	}
}
