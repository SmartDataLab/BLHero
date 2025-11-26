package com.xiugou.x1.design.module;


import com.xiugou.x1.design.module.autogen.ArenaSeasonRewardAbstractCache;

@org.springframework.stereotype.Component
public class ArenaSeasonRewardCache extends ArenaSeasonRewardAbstractCache<ArenaSeasonRewardAbstractCache.ArenaSeasonRewardCfg> {
	
	public ArenaSeasonRewardCfg getReward(int rank) {
		for(ArenaSeasonRewardCfg rewardCfg : this.all()) {
			if(rewardCfg.getRankUp() <= rank && (rewardCfg.getRankDown() == 0 || rank < rewardCfg.getRankDown())) {
				return rewardCfg;
			}
		}
		return this.all().get(this.all().size() - 1);
	}
	
}