/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1001_zhan_ling_exp", comment = "经验类战令", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }) })
public class ZhanLingExp extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(name = "type_name", comment = "活动类型名字", readonly = true)
	private String typeName;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(comment = "等级")
	private int level;
	@Column(comment = "经验值")
	private long exp;
	@Column(name = "free_reward_lv", comment = "已领取到的免费奖励等级")
	private int freeRewardLv;
	@Column(name = "premium_reward_lv", comment = "已领取到的高级奖励等级")
	private int premiumRewardLv;
	@Column(name = "buy_premium", comment = "是否已经购买高级通行证")
	private boolean buyPremium;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getFreeRewardLv() {
		return freeRewardLv;
	}

	public void setFreeRewardLv(int freeRewardLv) {
		this.freeRewardLv = freeRewardLv;
	}

	public int getPremiumRewardLv() {
		return premiumRewardLv;
	}

	public void setPremiumRewardLv(int premiumRewardLv) {
		this.premiumRewardLv = premiumRewardLv;
	}

	public boolean isBuyPremium() {
		return buyPremium;
	}

	public void setBuyPremium(boolean buyPremium) {
		this.buyPremium = buyPremium;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}
}
