/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
@Table(name = "p1005_xslb_player", comment = "限时礼包玩家数据", dbAlias = "game", asyncType = AsyncType.INSERT)
public class XianShiLiBaoPlayer extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
	@Column(name = "daily_limits", comment = "每日触发次数限制", length = 5000)
	private Map<Integer, Integer> dailyLimits = new HashMap<>();
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "life_limits", comment = "终生触发次数限制", length = 5000)
	private Map<Integer, Integer> lifeLimits = new HashMap<>(); 
	
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
	public Map<Integer, Integer> getDailyLimits() {
		return dailyLimits;
	}
	public void setDailyLimits(Map<Integer, Integer> dailyLimits) {
		this.dailyLimits = dailyLimits;
	}
	public Map<Integer, Integer> getLifeLimits() {
		return lifeLimits;
	}
	public void setLifeLimits(Map<Integer, Integer> lifeLimits) {
		this.lifeLimits = lifeLimits;
	}
}
