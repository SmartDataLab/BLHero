package com.xiugou.x1.game.server.module.bag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.UseItemType;
import com.xiugou.x1.design.module.GiftGroupCache;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

/**
 * @author yh
 * @date 2023/8/1
 * @apiNote
 */
@Service
public class RandomGiftService extends AbsUseItemService {
	@Autowired
	private ThingService thingService;
	@Autowired
	private ItemCache itemCache;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private GiftGroupCache giftGroupCache;

	@Override
	public UseItemType useItemType() {
		return UseItemType.RANDOM_ITEM;
	}

	@Override
	public void use(long pid, int itemId, long num, int option) {
		ItemCfg itemCfg = itemCache.getOrThrow(itemId);
		int giftId = Integer.parseInt(itemCfg.getUseData());
		Player player = playerService.getEntity(pid);
		
		List<RewardThing> rewardThings = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			rewardThings.addAll(giftGroupCache.getRewardByGiftId(player.getLevel(), itemCfg.getUseType(), giftId));
		}
		thingService.cost(pid, CostThing.of(itemId, num), GameCause.BAG_RANDOM_USE_ITEM);
		thingService.add(pid, rewardThings, GameCause.BAG_RANDOM_USE_ITEM);
	}
}
