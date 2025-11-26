package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model;

import java.time.LocalDateTime;

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
 * @author yh
 * @date 2023/9/25
 * @apiNote
 */
@Repository
@JvmCache(relation = {"pid", "giftId"})
@Table(name = "p1005_xian_shi_li_bao", comment = "限时礼包", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
        @Index(name = "pid", columns = {"pid"})})
public class XianShiLiBao extends AbstractEntity {
    @Id(strategy = Strategy.AUTO)
    @Column(comment = "id")
    private long id;
    @Column(comment = "玩家ID", readonly = true)
    private long pid;
    @Column(name = "gift_id", comment = "礼包id", readonly = true)
    private int giftId;
    @Column(comment = "是否已购买")
    private boolean buy;
    @Column(name = "end_time", comment = "结束时间", readonly = true)
    private LocalDateTime endTime;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }


    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
