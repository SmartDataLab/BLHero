package com.xiugou.x1.design.module;


import java.util.ArrayList;
import java.util.List;

import org.gaming.ruler.util.DropUtil;
import org.gaming.ruler.util.DropUtil.IDrop;

import com.xiugou.x1.design.module.autogen.ItemRandomPackAbstractCache;
import com.xiugou.x1.design.struct.RandomItem;
import com.xiugou.x1.design.struct.RewardThing;

@org.springframework.stereotype.Component
public class ItemRandomPackCache extends ItemRandomPackAbstractCache<ItemRandomPackCache.ItemRandomPackConfig> {

    public static class ItemRandomPackConfig extends ItemRandomPackAbstractCache.ItemRandomPackCfg implements IDrop {

        @Override
        public int getDropId() {
            return this.getId();
        }

        @Override
        public int getDropRate() {
            return this.getWeight();
        }
    }
    
    /**
     * 从掉落组内掉落奖励
     *
     * @param groupId
     * @return
     */
    public RewardThing randomReward(int groupId) {
        List<ItemRandomPackConfig> configs = this.getInGroupCollector(groupId);
        ItemRandomPackConfig config = DropUtil.randomDrop(configs);
        RandomItem item = DropUtil.randomIn10000Ratio(config.getReward());
        if (item == null) {
            return null;
        }
        return RewardThing.of(item.getItemId(), item.getNum());
    }

    public List<RewardThing> randomReward(List<Integer> groupIds) {
        ArrayList<RewardThing> rewardThings = new ArrayList<>();
        for (Integer groupId : groupIds) {
            RewardThing rewardThing = randomReward(groupId);
            if (rewardThing != null && rewardThing.getNum() > 0) {
                rewardThings.add(rewardThing);
            } else if(rewardThing != null && rewardThing.getNum() == 0) {
            	logger.error("{}配置分组{}掉落出来的物品数量为0", this.fileName(), groupId);
            }
        }
        return rewardThings;
    }

	@Override
	protected boolean check() {
		boolean hasError = false;
		for(ItemRandomPackConfig cfg : this.all()) {
			for(RandomItem randomItem : cfg.getReward()) {
				if(randomItem.getNum() <= 0) {
					logger.error("掉落配置{}有0值{}", this.fileName(), cfg.getId());
					hasError = true;
				}
			}
		}
		return !hasError;
	}
}