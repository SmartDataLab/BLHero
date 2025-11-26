/**
 * 
 */
package com.xiugou.x1.backstage.module.issueplatform.wabo.model;

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
@Table(name = "wabo_setting", comment = "wabo渠道参数设置", dbAlias = "backstage")
public class WaboSetting extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "game_id", comment = "渠道参数游戏ID")
	private String gameId = "15345";
	@Column(name = "game_key", comment = "游戏密钥")
	private String gameKey = "sdasd";
	@Column(name = "login_url", comment = "验证登录URL")
	private String loginUrl = "https://game-gateway.bepicgames.com/api/server/token";

	@Override
	public Long redisOwnerKey() {
		return 0L;
	}

	@Override
	public Long redisHashKey() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}
