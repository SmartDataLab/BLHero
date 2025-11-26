/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.model;

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
 * @author yy
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "game_server", comment = "服务器表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class GameServer extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "服务器ID")
	private int id;
	@Column(name = "avg_fighting", comment = "平均战力")
	private long avgFighting;
	@Column(name = "open_time", comment = "开服时间")
	private LocalDateTime openTime = LocalDateTime.now();
	@Column(comment = "是否已开服")
	private boolean open;
	@Column(name = "merge_to_server", comment = "合服至哪个服务器")
	private int mergeToServer;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getAvgFighting() {
		return avgFighting;
	}
	public void setAvgFighting(long avgFighting) {
		this.avgFighting = avgFighting;
	}
	public LocalDateTime getOpenTime() {
		return openTime;
	}
	public void setOpenTime(LocalDateTime openTime) {
		this.openTime = openTime;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public int getMergeToServer() {
		return mergeToServer;
	}
	public void setMergeToServer(int mergeToServer) {
		this.mergeToServer = mergeToServer;
	}
	
}
