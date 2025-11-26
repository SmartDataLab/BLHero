/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.model;

import java.time.LocalDateTime;

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
@Table(name = "tower", comment = "试练塔数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Tower extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "normal_layer", comment = "普通塔已通关层数")
	private int normalLayer;
	@Column(name = "strength_layer", comment = "力量塔已通关层数")
	private int strengthLayer;
	@Column(name = "agility_layer", comment = "敏捷塔已通关层数")
	private int agilityLayer;
	@Column(name = "wisdom_layer", comment = "智力塔已通关层数")
	private int wisdomLayer;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getNormalLayer() {
		return normalLayer;
	}
	public void setNormalLayer(int normalLayer) {
		this.normalLayer = normalLayer;
	}
	public int getStrengthLayer() {
		return strengthLayer;
	}
	public void setStrengthLayer(int strengthLayer) {
		this.strengthLayer = strengthLayer;
	}
	public int getAgilityLayer() {
		return agilityLayer;
	}
	public void setAgilityLayer(int agilityLayer) {
		this.agilityLayer = agilityLayer;
	}
	public int getWisdomLayer() {
		return wisdomLayer;
	}
	public void setWisdomLayer(int wisdomLayer) {
		this.wisdomLayer = wisdomLayer;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
}
