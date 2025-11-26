package com.xiugou.x1.design.module;


import java.util.List;

import com.xiugou.x1.design.module.autogen.PurgatoryRankAbstractCache;
import com.xiugou.x1.design.struct.RewardThing;

@org.springframework.stereotype.Component
public class PurgatoryRankCache extends PurgatoryRankAbstractCache<PurgatoryRankAbstractCache.PurgatoryRankCfg> {
	
	public List<RewardThing> getRankReward(int rank) {
        for (PurgatoryRankCfg purgatoryCfg : this.all()) {
            if (purgatoryCfg.getRankMax() <= rank && rank <= purgatoryCfg.getRankMin()) {
                return purgatoryCfg.getReward();
            }
        }
        return this.all().get(this.all().size() - 1).getReward();
	}
}