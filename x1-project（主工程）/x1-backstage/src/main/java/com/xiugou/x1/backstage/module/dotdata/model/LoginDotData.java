/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "dot_data_login", comment = "登录打点数据", dbAlias = "backlog", indexs = {
		@Index(name = "channelid_datestr", columns = { "channel_id", "date_str" }) }, byColumn = "time")
public class LoginDotData extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
	@Column(name = "open_id", comment = "账号ID")
	private String openId;
	@Column(name = "date_str", comment = "日期字符串，yyyyMMdd")
	private String dateStr;
	@Column(comment = "打点时机")
	private int timing;
	@Column(comment = "发生时间")
	private LocalDateTime time = LocalDateTime.now();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public int getTiming() {
		return timing;
	}

	public void setTiming(int timing) {
		this.timing = timing;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
