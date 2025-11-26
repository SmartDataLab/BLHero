/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Table;
import org.gaming.prefab.promotion.AbstractPromotionControl;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "promotion_control_end", comment = "已结束的活动控制表", dbAlias = "log")
public class PromotionControlLog extends AbstractPromotionControl {
	@Column(name = "control_id", comment = "原活动控制ID")
	private long controlId;
	@Column(name = "server_id", comment = "服务器ID")
	private int serverId;
	@Column(name = "config_id", comment = "使用的配置ID")
	private int configId;

	public long getControlId() {
		return controlId;
	}

	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}
}
