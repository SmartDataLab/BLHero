package com.xiugou.x1.game.server.module.rank.service;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.model.RankReward;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote
 */
@Service
public class RankRewardService extends OneToOneService<RankReward> implements Lifecycle {
	
	@Override
	protected RankReward createWhenNull(long type) {
		RankReward rankReward = new RankReward();
		rankReward.setType((int) type);
		RankType rankType = RankType.getRankType((int) type);
		rankReward.setName(rankType.getDesc());
		return rankReward;
	}

	@Override
	public void start() throws Exception {
		for (RankType rank : RankType.values()) {
			getEntity(rank.getValue());
		}
	}
}
