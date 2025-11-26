package com.xiugou.x1.design.module;


import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.util.DropUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.autogen.GiftGroupAbstractCache;
import com.xiugou.x1.design.struct.RandomItem;
import com.xiugou.x1.design.struct.RewardThing;

@org.springframework.stereotype.Component
public class GiftGroupCache extends GiftGroupAbstractCache<GiftGroupAbstractCache.GiftGroupCfg> {
	
    @Autowired
    private GiftGroupCache giftGroupCache;

    public List<RewardThing> getRewardByGiftId(int level, int type, int groupId) {
    	Asserts.isTrue(type == 1, TipsCode.BAG_GIFT_CONFIG_ERROR);
    	
        List<GiftGroupCfg> giftGroupCfg = giftGroupCache.getInTypeGroupIdCollector(type, groupId);
        GiftGroupCfg groupCfg = null;

        //匹配对应等级的礼品
        for (GiftGroupCfg giftGroupConfig : giftGroupCfg) {
            if (giftGroupConfig.getLevel() > level) {
                break;
            }
            groupCfg = giftGroupConfig;
        }
        Asserts.isTrue(groupCfg != null, TipsCode.BAG_GIFT_CONFIG_ERROR);
        
        List<RewardThing> rewardThings = new ArrayList<>();
        //固定奖励
        rewardThings.addAll(groupCfg.getFixationReward());
        //随机奖励
        List<RandomItem> randomReward = groupCfg.getRandomReward();
		if (!randomReward.isEmpty()) {
			RandomItem randomItem = DropUtil.randomIn10000Ratio(randomReward);
			if (randomItem != null) {
				rewardThings.add(RewardThing.of(randomItem.getItemId(), randomItem.getNum()));
			}
		}
        return rewardThings;
    }

	@Override
	protected boolean check() {
		boolean correct = true;
		for(GiftGroupCfg cfg : this.all()) {
			if(cfg.getType() == 1) {
				if(cfg.getRandomReward().isEmpty() && cfg.getFixationReward().isEmpty()) {
					logger.error("{}中的礼包组{}没有配随机奖励", fileName(), cfg.getGroupId());
					correct = false;
				}
				if(!cfg.getOptionReward().isEmpty()) {
					logger.error("{}中的礼包组{}不需要配自选奖励", fileName(), cfg.getGroupId());
					correct = false;
				}
			} else {
				if(cfg.getOptionReward().isEmpty()) {
					logger.error("{}中的礼包组{}没有配自选奖励", fileName(), cfg.getGroupId());
					correct = false;
				}
				if(!cfg.getRandomReward().isEmpty() || !cfg.getFixationReward().isEmpty()) {
					logger.error("{}中的礼包组{}不需要配随机奖励", fileName(), cfg.getGroupId());
					correct = false;
				}
			}
		}
		return correct;
	}
}