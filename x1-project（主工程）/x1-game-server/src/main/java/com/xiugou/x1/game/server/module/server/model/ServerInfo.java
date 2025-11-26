/**
 *
 */
package com.xiugou.x1.game.server.module.server.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 */
@Repository
@JvmCache(cacheTime = 0)
@Table(name = "server_info", comment = "服务器信息表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class ServerInfo extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "服务器ID")
	private long id;
	@Column(comment = "是否已经开服")
	private boolean opened;
	@Column(comment = "是否维护中")
	private boolean maintain;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "open_time", comment = "开服时间")
	private LocalDateTime openTime = LocalDateTime.now();
	@Column(name = "online_count_time", comment = "在线人数统计时间")
	private LocalDateTime onlineCountTime = LocalDateTime.now();
	@Column(name = "online_scatter_time", comment = "在线时长占比统计时间")
	private LocalDateTime onlineScatterTime = LocalDateTime.now();
	@Column(name = "online_5minute_time", comment = "5分钟在线人数统计时间")
	private LocalDateTime online5minuteTime = LocalDateTime.now();
	@Column(name = "resume_count_time", comment = "游戏汇总统计时间")
	private LocalDateTime resumeCountTime = LocalDateTime.now();
	@Column(name = "heart_beat_time", comment = "心跳时间")
	private LocalDateTime heartBeatTime = LocalDateTime.now();
	@Column(name = "cross_ip", comment = "跨服IP")
	private String crossIp;
	@Column(name = "cross_port", comment = "跨服端口")
	private String crossPort;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalDateTime openTime) {
		this.openTime = openTime;
	}

	public LocalDateTime getOnlineCountTime() {
		return onlineCountTime;
	}

	public void setOnlineCountTime(LocalDateTime onlineCountTime) {
		this.onlineCountTime = onlineCountTime;
	}

	public boolean isMaintain() {
		return maintain;
	}

	public void setMaintain(boolean maintain) {
		this.maintain = maintain;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public LocalDateTime getOnlineScatterTime() {
		return onlineScatterTime;
	}

	public void setOnlineScatterTime(LocalDateTime onlineScatterTime) {
		this.onlineScatterTime = onlineScatterTime;
	}

	public LocalDateTime getOnline5minuteTime() {
		return online5minuteTime;
	}

	public void setOnline5minuteTime(LocalDateTime online5minuteTime) {
		this.online5minuteTime = online5minuteTime;
	}

	public LocalDateTime getResumeCountTime() {
		return resumeCountTime;
	}

	public void setResumeCountTime(LocalDateTime resumeCountTime) {
		this.resumeCountTime = resumeCountTime;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public LocalDateTime getHeartBeatTime() {
		return heartBeatTime;
	}

	public void setHeartBeatTime(LocalDateTime heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}

	public String getCrossIp() {
		return crossIp;
	}

	public void setCrossIp(String crossIp) {
		this.crossIp = crossIp;
	}

	public String getCrossPort() {
		return crossPort;
	}

	public void setCrossPort(String crossPort) {
		this.crossPort = crossPort;
	}
}
