/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.yile.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "yile_setting", comment = "益乐渠道参数设置", dbAlias = "backstage")
public class YiLeSetting extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "platform_id", comment = "益乐平台数据ID")
	private long platformId;
	@Column(name = "game_id", comment = "益乐平台数据ID")
	private String gameId = "10910";
	@Column(name = "product_key", comment = "益乐平台数据ID")
	private String productKey = "0f504f9466fb085b10be8fafe7debf65";
	@Column(name = "charge_key", comment = "益乐平台数据ID")
	private String chargeKey = "e581eea614f472e294cea85d75a2eb62";
	@Column(name = "login_url", comment = "益乐平台数据ID")
	private String loginUrl = "https://oursdk.bingchenghuyu.com/cp/loginVerify";
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPlatformId() {
		return platformId;
	}
	public void setPlatformId(long platformId) {
		this.platformId = platformId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	public String getChargeKey() {
		return chargeKey;
	}
	public void setChargeKey(String chargeKey) {
		this.chargeKey = chargeKey;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return id;
	}
}
