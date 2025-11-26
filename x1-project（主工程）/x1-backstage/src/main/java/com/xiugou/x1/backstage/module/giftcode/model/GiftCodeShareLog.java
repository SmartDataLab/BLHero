/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "gift_code_share_log", comment = "共享礼包码使用情况表", dbAlias = "backstage")
public class GiftCodeShareLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "记录ID")
	private long id;
	@Column(comment = "礼包码")
	private String code;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(name = "player_name", comment = "玩家名称")
	private String playerName;
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
}
