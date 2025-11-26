package com.xiugou.x1.game.server.module.bag.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.constant.UseItemType;
import com.xiugou.x1.design.module.GiftGroupCache;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.GiftGroupAbstractCache.GiftGroupCfg;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

/**
 * @author yh
 * @date 2023/8/1
 * @apiNote
 */
@Service
public class OptionalTypeService extends AbsUseItemService {
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private GiftGroupCache giftGroupCache;
    @Autowired
    private PlayerService playerService;

    @Override
    public UseItemType useItemType() {
        return UseItemType.OPTIONAL_ITEM;
    }

    @Override
    public void use(long pid, int itemId, long num, int option) {
        ItemCfg itemCfg = itemCache.getOrThrow(itemId);
        int useData = Integer.parseInt(itemCfg.getUseData());
        Player player = playerService.getEntity(pid);

        //筛选对应等级的礼包 并匹配玩家选择的奖励
        List<GiftGroupCfg> giftGroupCfg = giftGroupCache.getInTypeGroupIdCollector(itemCfg.getUseType(), useData);
        GiftGroupCfg groupCfg = null;
        for (GiftGroupCfg giftGroupConfig : giftGroupCfg) {
            if (giftGroupConfig.getLevel() > player.getLevel()) {
                break;
            }
            groupCfg = giftGroupConfig;
        }
        Asserts.isTrue(groupCfg != null, TipsCode.BAG_GIFT_CONFIG_ERROR);
        
        List<RewardThing> optionReward = groupCfg.getOptionReward();
        RewardThing rewardThing = optionReward.stream().filter(r -> r.getThingId() == option).findFirst().orElse(null);
        Asserts.isTrue(rewardThing != null, TipsCode.BAG_GIFT_CONFIG_ERROR);

        rewardThing = ThingUtil.multiplyReward(rewardThing, (int) num);
        thingService.cost(pid, CostThing.of(itemId, num), GameCause.BAG_OPTION_ITEM);
        thingService.add(pid, rewardThing, GameCause.BAG_OPTION_ITEM);
    }
}
