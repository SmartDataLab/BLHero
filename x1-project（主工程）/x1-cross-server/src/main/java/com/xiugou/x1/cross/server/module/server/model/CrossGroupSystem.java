/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yy
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "cross_group_system", comment = "跨服系统表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class CrossGroupSystem extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "系统ID")
	private long id;
	@Column(name = "next_group_time", comment = "下一次进行跨服分组的时间")
	private LocalDateTime nextGroupTime = LocalDateTime.now();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public LocalDateTime getNextGroupTime() {
		return nextGroupTime;
	}
	public void setNextGroupTime(LocalDateTime nextGroupTime) {
		this.nextGroupTime = nextGroupTime;
	}
}
