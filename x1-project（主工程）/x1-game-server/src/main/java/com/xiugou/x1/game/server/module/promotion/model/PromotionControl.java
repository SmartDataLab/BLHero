/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.prefab.promotion.AbstractPromotionControl;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(cacheTime = 0, loadAllOnStart = true, relation = {"serverId", "typeId"})
@Table(name = "promotion_control", comment = "活动控制表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "serverid_typeid", columns = { "server_id", "type_id" }, type = IndexType.UNIQUE) })
public class PromotionControl extends AbstractPromotionControl {
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(name = "config_id", comment = "使用的配置ID")
	private int configId;
	@Column(name = "settle_turns", comment = "当前结算的活动轮数")
	private int settleTurns;
	@Column(comment = "活动已经终结，不会再开")
	private boolean terminate;

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

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public int getSettleTurns() {
		return settleTurns;
	}

	public void setSettleTurns(int settleTurns) {
		this.settleTurns = settleTurns;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
}
