package com.xiugou.x1.design.module;


import java.util.List;

import com.xiugou.x1.design.module.autogen.VillageRankAbstractCache;
import com.xiugou.x1.design.struct.RewardThing;

@org.springframework.stereotype.Component
public class VillageRankCache extends VillageRankAbstractCache<VillageRankAbstractCache.VillageRankCfg> {

    public List<RewardThing> getRankReward(int rank) {
        for (VillageRankCfg villageRankCfg : this.all()) {
            if (villageRankCfg.getRankMax() <= rank && rank <= villageRankCfg.getRankMin()) {
                return villageRankCfg.getReward();
            }
        }
        return this.all().get(this.all().size() - 1).getReward();
    }

}