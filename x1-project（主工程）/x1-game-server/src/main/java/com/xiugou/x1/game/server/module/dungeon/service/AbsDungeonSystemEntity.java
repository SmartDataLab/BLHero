package com.xiugou.x1.game.server.module.dungeon.service;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.orm.AbstractEntity;

/**
 * @author yh
 * @date 2023/8/18
 * @apiNote 简单副本统一实体基类
 */
@MappedSuperclass(sort = MappedSuperclass.Priority._2)
public abstract class AbsDungeonSystemEntity extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(name = "server_id", comment = "服务器ID")
	private int serverId;
	@Column(comment = "赛季")
	private int round;
	@Column(name = "next_reset_time", comment = "下次重置时间")
	private LocalDateTime nextResetTime = LocalDateTime.now();

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public LocalDateTime getNextResetTime() {
		return nextResetTime;
	}

	public void setNextResetTime(LocalDateTime nextResetTime) {
		this.nextResetTime = nextResetTime;
	}
}
