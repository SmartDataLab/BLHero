package com.xiugou.x1.game.server.module.villagedefense.service;

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
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;

import pojo.xiugou.x1.pojo.log.village.VillageLog;

/**
 * @author yh
 * @date 2023/8/17
 * @apiNote
 */
@Component
public class VillageStorer extends NumberThingStorer<VillageLog> {
    @Autowired
    private VillageService villageService;
    @Autowired
    private PlayerService playerService;

    @Override
    protected VillageLog newLog() {
        return new VillageLog();
    }

    @Override
    protected BaseRepository<VillageLog> initRepository() {
        return SlimDao.getRepository(VillageLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.VILLAGE_POINT;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.VILLAGE_POINT_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return villageService.getEntity(entityId).getPoint();
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        long point = 0;
        for (int i = 0; i < rewardThings.size(); i++) {
            point += rewardThings.get(i).getNum();
        }
        Village village = villageService.getEntity(entityId);
        village.setPoint(village.getPoint() + point);
        villageService.update(village);
        return new RewardReceipt(rewardThings);
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        long point = 0;
        for (ICostThing costThing : costThings) {
            point += costThing.getNum();
        }
        Village village = villageService.getEntity(entityId);
        village.setPoint(village.getPoint() - point);
        villageService.update(village);
        return new CostReceipt(costThings);
    }

    @Override
    protected String getOwnerName(long entityId) {
        Player player = playerService.getEntity(entityId);
        return player.getNick();
    }
}
