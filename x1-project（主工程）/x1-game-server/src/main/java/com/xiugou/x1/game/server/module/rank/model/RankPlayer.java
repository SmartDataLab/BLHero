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

import com.xiugou.x1.game.server.module.rank.struct.RankRewardData;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "rank_player", comment = "排行榜玩家奖励记录表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class RankPlayer extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "type_rewards", comment = "类型和奖励领取情况", length = 5000)
	private Map<Integer, RankRewardData> typeRewards = new HashMap<>();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public Map<Integer, RankRewardData> getTypeRewards() {
		return typeRewards;
	}

	public void setTypeRewards(Map<Integer, RankRewardData> typeRewards) {
		this.typeRewards = typeRewards;
	}
}
