/**
 * 
 */
package com.xiugou.x1.backstage.module.clientversion.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "client_version", comment = "客户端版本控制", dbAlias = "backstage", indexs = {
		@Index(name = "channel_version", columns = { "channel_id", "version_code" }, type = IndexType.UNIQUE) })
public class ClientVersion extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "channel_name", comment = "渠道名字")
	private String channelName;
	@Column(name = "version_code", comment = "版本编号", readonly = true)
	private int versionCode;
	@Column(name = "server_type", comment = "服务器类型，1测试，2审核，3正式")
	private int serverType;
	@Column(name = "remote_url", comment = "远程地址")
	private String remoteUrl;
	@Column(name = "resource_version", comment = "客户端资源版本号")
	private String resourceVersion;
	@Column(comment = "备注")
	private String remark;
	@Column(name = "pc_resource_version", comment = "PC客户端资源版本号")
	private String pcResourceVersion;
	@Column(name = "quick_url", comment = "快速通道地址")
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

	@Override
	public Long redisOwnerKey() {
		return channelId;
	}

	@Override
	public Long redisHashKey() {
		return (long)versionCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
