package com.xiugou.x1.design.module;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.UseItemType;
import com.xiugou.x1.design.module.autogen.GiftGroupAbstractCache.GiftGroupCfg;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache;
import com.xiugou.x1.design.struct.RandomItem;
import com.xiugou.x1.design.struct.RewardThing;

@org.springframework.stereotype.Component
public class ItemCache extends ItemAbstractCache<ItemAbstractCache.ItemCfg> {

	@Autowired
	private GiftGroupCache giftGroupCache;
	
	@Override
	protected boolean check() {
		boolean correct = true;
		for(ItemCfg itemCfg : this.all()) {
			if(itemCfg.getKind() == ItemType.AUTO_USE_GIFT.getType()) {
				//检查自动使用礼包是否套娃
				List<GiftGroupCfg> gifts = giftGroupCache.findInTypeGroupIdCollector(1, itemCfg.getKindParam());
				if(gifts == null) {
					logger.info("{}道具[{}-{}]中配置的礼包未在礼包表找到配置", fileName(), itemCfg.getId(), itemCfg.getName());
					correct = false;
				}
				for(GiftGroupCfg cfg : gifts) {
					for(RandomItem rewardThing : cfg.getRandomReward()) {
						ItemCfg insideItem = this.getOrNull(rewardThing.getItemId());
						if(insideItem == null) {
							logger.info("{}中的配置{}随机奖励{}未找到道具配置", giftGroupCache.fileName(), cfg.getId(), rewardThing.getItemId());
							correct = false;
						} else if(insideItem.getKind() == ItemType.AUTO_USE_GIFT.getType()) {
							logger.info("{}道具[{}-{}]中配置的礼包套娃自动使用礼包", fileName(), itemCfg.getId(), itemCfg.getName());
							correct = false;
						}
					}
					for(RewardThing rewardThing : cfg.getFixationReward()) {
						ItemCfg insideItem = this.getOrNull(rewardThing.getThingId());
						if(insideItem == null) {
							logger.info("{}中的配置{}固定奖励{}未找到道具配置", giftGroupCache.fileName(), cfg.getId(), rewardThing.getThingId());
							correct = false;
						} else if(insideItem.getKind() == ItemType.AUTO_USE_GIFT.getType()) {
							logger.info("{}道具[{}-{}]中配置的礼包套娃自动使用礼包", fileName(), itemCfg.getId(), itemCfg.getName());
							correct = false;
						}
					}
				}
			} else if(itemCfg.getKind() == ItemType.ITEM.getType()) {
				if(itemCfg.getUseType() == UseItemType.RANDOM_ITEM.getId()) {
					String[] parts = itemCfg.getUseData().split("#");
					for(String part : parts) {
						Integer groupId = Integer.parseInt(part);
						List<GiftGroupCfg> gifts = giftGroupCache.findInTypeGroupIdCollector(1, groupId);
						if(gifts == null) {
							logger.info("{}道具[{}-{}]中配置的随机礼包未在礼包表找到配置", fileName(), itemCfg.getId(), itemCfg.getName());
							correct = false;
						}
					}
				} else if(itemCfg.getUseType() == UseItemType.OPTIONAL_ITEM.getId()) {
					Integer groupId = Integer.parseInt(itemCfg.getUseData());
					List<GiftGroupCfg> gifts = giftGroupCache.findInTypeGroupIdCollector(2, groupId);
					if(gifts == null) {
						logger.info("{}道具[{}-{}]中配置的自选礼包未在礼包表找到配置", fileName(), itemCfg.getId(), itemCfg.getName());
						correct = false;
					}
				}
			}
		}
		return correct;
	}
}