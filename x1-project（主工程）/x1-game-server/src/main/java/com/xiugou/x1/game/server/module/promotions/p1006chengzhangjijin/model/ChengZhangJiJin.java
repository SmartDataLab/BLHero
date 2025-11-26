/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.model;

import java.util.HashSet;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = {"pid", "typeId"})
@Table(name = "p1006_cheng_zhang_ji_jin", comment = "成长基金", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
        @Index(name = "pid", columns = {"pid"})})
public class ChengZhangJiJin extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "唯一ID")
    private long id;
    @Column(comment = "玩家ID", readonly = true)
    private long pid;
    @Column(name = "type_id", comment = "活动类型ID", readonly = true)
    private int typeId;
    @Column(comment = "期数")
    private int round;
    @Column(comment = "是否已购买本期基金")
    private boolean open;
    @Column(name = "reward_ids", comment = "已领取的奖励ID")
    private Set<Integer> rewardIds = new HashSet<>();
    
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
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public Set<Integer> getRewardIds() {
		return rewardIds;
	}
	public void setRewardIds(Set<Integer> rewardIds) {
		this.rewardIds = rewardIds;
	}
}
