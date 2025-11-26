package com.xiugou.x1.game.server.module.dayRecharge.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "day_recharge", comment = "每日首充表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class DayRecharge extends AbstractEntity implements DailyResetEntity {
    @Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "buy_all", comment = "一键购买")
    private boolean buyAll;
    @Column(name = "free_reward", comment = "是否已领取免费好礼")
    private boolean freeReward;
    @Column(name = "day_recharge", comment = "散买商品列表")
    private List<Integer> dayRecharge = new ArrayList<>();
    @Column(name = "daily_time", comment = "重置时间")
    private LocalDateTime dailyTime = LocalDateTime.now();
    @Column(name = "buy_round", comment = "当前可购轮次")
    private int buyRound;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public boolean isBuyAll() {
        return buyAll;
    }

    public void setBuyAll(boolean buyAll) {
        this.buyAll = buyAll;
    }

    public boolean isFreeReward() {
        return freeReward;
    }

    public void setFreeReward(boolean freeReward) {
        this.freeReward = freeReward;
    }

    public List<Integer> getDayRecharge() {
        return dayRecharge;
    }

    public void setDayRecharge(List<Integer> dayRecharge) {
        this.dayRecharge = dayRecharge;
    }

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public int getBuyRound() {
		return buyRound;
	}

	public void setBuyRound(int buyRound) {
		this.buyRound = buyRound;
	}
}
