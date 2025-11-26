package com.xiugou.x1.game.server.module.treasureHouse.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/23
 * @apiNote
 */
@Repository
@JvmCache(relation = { "pid", "shopId" })
@Table(name = "treasure", comment = "珍宝阁购买记录表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_shopid", columns = { "pid", "shop_id" }, type = IndexType.UNIQUE) })
public class Treasure extends AbstractEntity {
	@Id(strategy = Id.Strategy.AUTO)
	@Column(comment = "id")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "shop_id", comment = "商城Id", readonly = true)
	private int shopId;
	@Column(name = "product_detail", comment = "商品详情", length = 500)
	private Map<Integer, Integer> productDetail = new HashMap<>();
	@Column(name = "limit_product_detail", comment = "终生限购商品详情", length = 500)
	private Map<Integer, Integer> limitProductDetail = new HashMap<>();
	@Column(comment = "当前期数")
	private int period;
	@Column(name = "next_reset_time", comment = "下次重置时间")
	private LocalDateTime nextResetTime = LocalDateTime.now();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public Map<Integer, Integer> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(Map<Integer, Integer> productDetail) {
		this.productDetail = productDetail;
	}

	public LocalDateTime getNextResetTime() {
		return nextResetTime;
	}

	public void setNextResetTime(LocalDateTime nextResetTime) {
		this.nextResetTime = nextResetTime;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Map<Integer, Integer> getLimitProductDetail() {
		return limitProductDetail;
	}

	public void setLimitProductDetail(Map<Integer, Integer> limitProductDetail) {
		this.limitProductDetail = limitProductDetail;
	}
}
