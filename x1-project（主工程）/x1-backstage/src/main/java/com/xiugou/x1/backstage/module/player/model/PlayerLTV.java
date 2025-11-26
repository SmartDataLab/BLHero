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
@Table(name = "player_ltv", comment = "玩家生命周期价值LTV表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid_born_daycount", columns = { "channel_id", "born", "day_count" }, type = IndexType.UNIQUE) })
public class PlayerLTV extends AbstractEntity {
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
	@Column(comment = "总支付金额，单位分")
	private long money;
	@Column(name = "player_count", comment = "创号玩家数量")
	private int playerCount;
	
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
	public long getServerUid() {
		return serverUid;
	}
	public void setServerUid(long serverUid) {
		this.serverUid = serverUid;
	}
	public String getBorn() {
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public int getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
}
