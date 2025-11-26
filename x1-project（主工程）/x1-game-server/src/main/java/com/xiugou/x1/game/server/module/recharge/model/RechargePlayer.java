/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "recharge_player", comment = "玩家充值数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class RechargePlayer extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
	@Column(name = "total_pay", comment = "历史总支付金额（含虚拟充值）")
	private long totalPay;
	@Column(name = "real_total_pay", comment = "真实历史总支付金额（不含虚拟充值）")
	private long realTotalPay;
	@Column(name = "daily_pay", comment = "今日支付金额（含虚拟充值）")
	private long dailyPay;
	@Column(name = "real_daily_pay", comment = "真实今日支付金额（不含虚拟充值）")
	private long realDailyPay;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "buy_products", comment = "购买过的充值商品", extra = "text")
    private Set<Integer> buyProducts = new HashSet<>();
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public Set<Integer> getBuyProducts() {
		return buyProducts;
	}
	public void setBuyProducts(Set<Integer> buyProducts) {
		this.buyProducts = buyProducts;
	}
	public long getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(long totalPay) {
		this.totalPay = totalPay;
	}
	public long getRealTotalPay() {
		return realTotalPay;
	}
	public void setRealTotalPay(long realTotalPay) {
		this.realTotalPay = realTotalPay;
	}
	public long getDailyPay() {
		return dailyPay;
	}
	public void setDailyPay(long dailyPay) {
		this.dailyPay = dailyPay;
	}
	public long getRealDailyPay() {
		return realDailyPay;
	}
	public void setRealDailyPay(long realDailyPay) {
		this.realDailyPay = realDailyPay;
	}
}
