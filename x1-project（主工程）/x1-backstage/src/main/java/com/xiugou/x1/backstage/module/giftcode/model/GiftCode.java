/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.model;

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
@Table(name = "gift_code", comment = "礼包码表", dbAlias = "backstage", indexs = {
		@Index(name = "code", columns = { "code" }, type = IndexType.UNIQUE) })
public class GiftCode extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "记录ID")
	private long id;
	@Column(comment = "礼包码", readonly = true)
	private String code;
	@Column(name = "channel_id", comment = "所属渠道ID", readonly = true)
	private long channelId;
	@Column(comment = "礼包码类型，1通码，2独享码", readonly = true)
	private int type;
	@Column(name = "config_id", comment = "配置礼包ID", readonly = true)
	private int configId;
	@Column(name = "config_name", comment = "配置礼包名称", readonly = true)
	private String configName;
	@Column(name = "user_id", comment = "创建的用户ID", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "创建的用户名称", readonly = true)
	private String userName;
	@Column(name = "player_id", comment = "使用码的玩家ID")
	private long playerId;
	@Column(name = "player_name", comment = "使用码的玩家名称")
	private String playerName;
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
}
