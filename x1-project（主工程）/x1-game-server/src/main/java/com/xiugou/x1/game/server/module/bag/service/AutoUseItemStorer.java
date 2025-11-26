package com.xiugou.x1.game.server.module.bag.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.CostReceipt;
import org.gaming.prefab.thing.ICostThing;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.NumberThingStorer;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.GiftGroupCache;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.bag.ItemLog;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Component
public class AutoUseItemStorer extends NumberThingStorer<ItemLog> {
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GiftGroupCache giftGroupCache;

    @Override
    protected ItemLog newLog() {
        return new ItemLog();
    }

    @Override
    protected BaseRepository<ItemLog> initRepository() {
        return SlimDao.getRepository(ItemLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.AUTO_USE_GIFT;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.BAG_ITEM_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return 0;
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        Player player = playerService.getEntity(entityId);
        List<RewardThing> rewardList = new ArrayList<>();
        String giftGroupRemark = "来自道具：";
        
        RewardReceipt giftGroupReceipt = new RewardReceipt();
        for (IRewardThing rewardThing : rewardThings) {
        	ItemCfg itemCfg = itemCache.getOrThrow(rewardThing.getThingId());
        	for(int i = 0; i < rewardThing.getNum(); i++) {
                rewardList.addAll(giftGroupCache.getRewardByGiftId(player.getLevel(), 1, itemCfg.getKindParam()));
        	}
            giftGroupRemark += rewardThing.getThingId() + "#" + rewardThing.getNum() + "|";
            
            giftGroupReceipt.append(new RewardDetail(rewardThing.getThingId(), rewardThing.getNum()));
        }
        //流水日志需要记录自动使用礼包的获得和通过礼包真实获得的道具
        //1、这里是自动礼包的获得日志
        this.afterAdd(entityId, giftGroupReceipt, cause, "");
        //TODO 这里要好好测试一下
        //2、这里是通过礼包真实获得的道具及其日志
        RewardReceipt rewardReceipt = thingService.add(entityId, rewardList, cause, NoticeType.NO, giftGroupRemark);
        return rewardReceipt;
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        throw new UnsupportedOperationException("不应该被调用的函数");
    }

    @Override
    protected String getOwnerName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }
}
