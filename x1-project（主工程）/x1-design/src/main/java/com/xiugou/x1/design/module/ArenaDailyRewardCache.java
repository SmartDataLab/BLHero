package com.xiugou.x1.design.module;


import com.xiugou.x1.design.module.autogen.ArenaDailyRewardAbstractCache;

@org.springframework.stereotype.Component
public class ArenaDailyRewardCache extends ArenaDailyRewardAbstractCache<ArenaDailyRewardAbstractCache.ArenaDailyRewardCfg> {
	
	public ArenaDailyRewardCfg getReward(int rank) {
		for(ArenaDailyRewardCfg rewardCfg : this.all()) {
			if(rewardCfg.getRankUp() <= rank && (rewardCfg.getRankDown() == 0 || rank < rewardCfg.getRankDown())) {
				return rewardCfg;
			}
		}
		return this.all().get(this.all().size() - 1);
	}
}