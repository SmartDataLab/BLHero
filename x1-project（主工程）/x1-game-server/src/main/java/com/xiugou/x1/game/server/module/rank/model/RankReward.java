package com.xiugou.x1.game.server.module.rank.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.rank.struct.RankFirstReach;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "rank_reward", comment = "排行榜奖励达成表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class RankReward extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "排行榜类型")
	private int type;
	@Column(comment = "排行榜名称", readonly = true)
	private String name;
	@Column(name = "reward_id", comment = "奖励ID")
	private int rewardId;
	@Column(name = "max_score", comment = "历史最高分")
	private long maxScore;
	@Column(name = "first_reachs", comment = "首位达成", extra = "text")
	private Map<Integer, RankFirstReach> firstReachs = new HashMap<>();

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public long getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(long maxScore) {
		this.maxScore = maxScore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, RankFirstReach> getFirstReachs() {
		return firstReachs;
	}

	public void setFirstReachs(Map<Integer, RankFirstReach> firstReachs) {
		this.firstReachs = firstReachs;
	}
}
