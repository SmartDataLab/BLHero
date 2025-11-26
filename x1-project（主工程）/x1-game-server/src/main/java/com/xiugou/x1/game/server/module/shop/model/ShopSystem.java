package com.xiugou.x1.game.server.module.shop.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/4
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "shop_system", comment = "商店系统表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ShopSystem extends AbstractEntity {
	@Id(strategy = Id.Strategy.IDENTITY)
	@Column(name = "shop_id", comment = "商城ID")
	private int shopId;
	@Column(name = "config_round", comment = "配置轮次")
	private int configRound;
	@Column(name = "increase_round", comment = "递增轮次")
	private int increaseRound;
	@Column(name = "next_reset", comment = "下次重置")
	private LocalDateTime nextReset = LocalDateTime.now();

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getConfigRound() {
		return configRound;
	}

	public void setConfigRound(int configRound) {
		this.configRound = configRound;
	}

	public int getIncreaseRound() {
		return increaseRound;
	}

	public void setIncreaseRound(int increaseRound) {
		this.increaseRound = increaseRound;
	}

	public LocalDateTime getNextReset() {
		return nextReset;
	}

	public void setNextReset(LocalDateTime nextReset) {
		this.nextReset = nextReset;
	}
}
