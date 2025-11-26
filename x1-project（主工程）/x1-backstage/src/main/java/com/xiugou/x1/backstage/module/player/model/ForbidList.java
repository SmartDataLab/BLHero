/**
 * 
 */
package com.xiugou.x1.backstage.module.player.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "forbid_list", comment = "封禁账号表", dbAlias = "backstage", indexs = {
		@Index(name = "channelid_openid", columns = { "channel_id", "open_id" }, type = IndexType.UNIQUE)})
public class ForbidList extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "唯一ID")
    private long id;
	@Column(name = "channel_id", comment = "渠道ID")
	private long channelId;
    @Column(name = "open_id", comment = "账户ID")
    private String openId;
    @Column(name = "user_id", comment = "操作人ID")
    private long userId;
    @Column(name = "user_name", comment = "操作人名字")
    private String userName;
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
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
