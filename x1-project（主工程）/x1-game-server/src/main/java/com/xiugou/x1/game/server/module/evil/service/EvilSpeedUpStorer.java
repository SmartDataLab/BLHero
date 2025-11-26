package com.xiugou.x1.game.server.module.evil.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.game.server.module.evil.model.EvilFurnace;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.evil.EvilSpeedUpLog;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Component
public class EvilSpeedUpStorer extends NumberThingStorer<EvilSpeedUpLog> {
    @Autowired
    private EvilFurnaceService evilFurnaceService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ItemCache itemCache;

    @Override
    protected EvilSpeedUpLog newLog() {
        return new EvilSpeedUpLog();
    }

    @Override
    protected BaseRepository<EvilSpeedUpLog> initRepository() {
        return SlimDao.getRepository(EvilSpeedUpLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.EVIL_SPEED_UP;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.EVI_SPEEDUP_TIME_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return evilFurnaceService.getEntity(entityId).getSpeedUpTime();
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        long time = 0;
        for (IRewardThing rewardThing : rewardThings) {
            ItemCfg itemCfg = itemCache.getOrThrow(rewardThing.getThingId());
            time += itemCfg.getKindParam() * rewardThing.getNum();
        }
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(entityId);
        evilFurnace.setSpeedUpTime(evilFurnace.getSpeedUpTime() + time);
        evilFurnaceService.update(evilFurnace);
        return new RewardReceipt(rewardThings);
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        long time = 0;
        for (ICostThing costThing : costThings) {
        	ItemCfg itemCfg = itemCache.getOrThrow(costThing.getThingId());
            time += itemCfg.getKindParam() * costThing.getNum();
        }
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(entityId);
        evilFurnace.setSpeedUpTime(evilFurnace.getSpeedUpTime() - time);
        evilFurnaceService.update(evilFurnace);
        return new CostReceipt(costThings);
    }

    @Override
    protected String getOwnerName(long entityId) {
        Player player = playerService.getEntity(entityId);
        return player.getNick();
    }
}
