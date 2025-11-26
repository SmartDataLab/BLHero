package com.xiugou.x1.game.server.module.trainingCamp.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;

/**
 * @author yh
 * @date 2023/6/5
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "training_camp", comment = "训练营表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class TrainingCamp extends AbstractEntity implements DailyResetEntity {
    @Id(strategy = Id.Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "max_peoples", comment = "高级训练解锁的上阵人数")
    private int maxPeoples;
    @Column(name = "level", comment = "普通训练等级")
    private int level;
    @Column(name = "expert_level", comment = "高级训练等级")
    private int expertLevel;
    //目前用于在跨天的时候重算最大队伍人数
    @Column(name = "daily_time", comment = "每日重置时间")
    private LocalDateTime dailyTime = LocalDateTime.now();

    public int getMaxPeoples() {
        return maxPeoples;
    }

    public void setMaxPeoples(int maxPeoples) {
        this.maxPeoples = maxPeoples;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExpertLevel() {
		return expertLevel;
	}

	public void setExpertLevel(int expertLevel) {
		this.expertLevel = expertLevel;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
}
