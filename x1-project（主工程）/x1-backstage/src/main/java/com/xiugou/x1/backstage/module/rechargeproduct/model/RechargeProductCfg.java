/**
 * 
 */
package com.xiugou.x1.backstage.module.rechargeproduct.model;

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
@Table(name = "recharge_product_cfg", comment = "商品配置", dbAlias = "backstage")
public class RechargeProductCfg extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "商品ID")
	private int id;
	@Column(comment = "商品名")
	private String name;
	@Column(comment = "商品描述")
	private String describe;
	@Column(comment = "金额，单位分")
	private long money;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return (long)id;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
}
