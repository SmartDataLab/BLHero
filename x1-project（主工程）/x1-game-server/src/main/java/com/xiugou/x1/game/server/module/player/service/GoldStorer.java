/**
 *
 */
package com.xiugou.x1.game.server.module.player.service;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.CostReceipt;
import org.gaming.prefab.thing.ICostThing;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.NumberThingStorer;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.module.player.event.CostGoldEvent;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.vip.constant.VipFuncType;
import com.xiugou.x1.game.server.module.vip.service.VipService;

import pojo.xiugou.x1.pojo.log.player.GoldLog;

/**
 * @author YY
 */
@Component
public class GoldStorer extends NumberThingStorer<GoldLog> {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private VipService privilegeBoostService;

    @Override
    protected GoldLog newLog() {
        return new GoldLog();
    }

    @Override
    protected BaseRepository<GoldLog> initRepository() {
        return SlimDao.getRepository(GoldLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.GOLD;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.GOLD_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return playerService.getEntity(entityId).getGold();
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        //VIP特权加成比例
        long total = 0;
        for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
        float boostRate = privilegeBoostService.boostRate(entityId, VipFuncType.MAINLINE_GOLD_RATE, cause.getCode());
        total = (long)(total * (1 + boostRate));
        
        Player player = playerService.getEntity(entityId);
        player.setGold(player.getGold() + total);
        playerService.update(player);
        
        return new RewardReceipt(rewardThings);
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        Player player = playerService.getEntity(entityId);
        for (ICostThing costThing : costThings) {
            player.setGold(player.getGold() - costThing.getNum());
        }
        playerService.update(player);
        
        EventBus.post(CostGoldEvent.of(entityId));
        return new CostReceipt(costThings);
    }

    @Override
    protected String getOwnerName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }
}
