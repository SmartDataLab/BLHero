package com.xiugou.x1.design.module;


import java.util.List;

import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.autogen.ActiveRankRewardsAbstractCache;

@Component
public class ActiveRankRewardsCache extends ActiveRankRewardsAbstractCache<ActiveRankRewardsAbstractCache.ActiveRankRewardsCfg> {
	
	/**
	 * @param promotionTypeId
	 * @param rank
	 * @return
	 */
	public ActiveRankRewardsCfg getRankReward(int promotionTypeId, int rank) {
		List<ActiveRankRewardsCfg> cfgList = this.getInActiveIdCollector(promotionTypeId);
		for(ActiveRankRewardsCfg cfg : cfgList) {
			if(cfg.getRankUp() <= rank && (cfg.getRankDown() == 0 || rank <= cfg.getRankDown())) {
				return cfg;
			}
		}
		return cfgList.get(cfgList.size() - 1);
	}
}