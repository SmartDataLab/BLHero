package com.xiugou.x1.game.server.module.shop.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.shop.struct.ProductDetailData;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
@Repository
@JvmCache(relation = { "pid", "shopId" })
@Table(name = "shop_player", comment = "兑换商城记录表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ShopPlayer extends AbstractEntity {
	@Id(strategy = Id.Strategy.AUTO)
	@Column(comment = "id")
	private long id;
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "shop_id", comment = "商城Id")
	private int shopId;
	@Column(name = "increase_round", comment = "递增轮次")
	private int increaseRound;
	@Column(name = "product_detail", comment = "商品详情", length = 5000)
	private Map<Integer, ProductDetailData> productDetail = new HashMap<>();

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

	public Map<Integer, ProductDetailData> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(Map<Integer, ProductDetailData> productDetail) {
		this.productDetail = productDetail;
	}

	public int getIncreaseRound() {
		return increaseRound;
	}

	public void setIncreaseRound(int increaseRound) {
		this.increaseRound = increaseRound;
	}
}
