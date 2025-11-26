/**
 * 
 */
package com.xiugou.x1.backstage.module.player.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "player_remain", comment = "玩家留存表", dbAlias = "backstage", indexs = {
		@Index(name = "csbd", columns = { "channel_id", "server_uid", "born", "day_count" }, type = IndexType.UNIQUE) })
public class PlayerRemain extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "server_uid", comment = "服务器唯一ID", readonly = true)
	private long serverUid;
	@Column(comment = "创号日期，yyyyMMdd", readonly = true)
	private String born;
	@Column(name = "day_count", comment = "距离创号时间的天数", readonly = true)
	private int dayCount;
	@Column(comment = "人数")
	private int player;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getBorn() {
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
	}
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
}
