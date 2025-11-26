package com.xiugou.x1.game.server.module.recruit.service;

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
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recruit.model.Recruit;

import pojo.xiugou.x1.pojo.log.recruit.RecruitPointsLog;

/**
 * @author yh
 * @date 2023/6/21
 * @apiNote
 */
@Component
public class RecruitPointStorer extends NumberThingStorer<RecruitPointsLog> {
	
    @Autowired
    private RecruitService rcruitService;
    @Autowired
    private PlayerService playerService;

    @Override
    protected RecruitPointsLog newLog() {
        return new RecruitPointsLog();
    }

    @Override
    protected BaseRepository<RecruitPointsLog> initRepository() {
        return SlimDao.getRepository(RecruitPointsLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.RECRUIT_POINT;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.RECRUIT_POINTIPS_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return rcruitService.getEntity(entityId).getPoints();
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        Recruit recruit = rcruitService.getEntity(entityId);
        for (IRewardThing rewardThing : rewardThings) {
            recruit.setPoints(recruit.getPoints() + rewardThing.getNum());
        }
        rcruitService.update(recruit);
        return new RewardReceipt(rewardThings);
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        Recruit recruit = rcruitService.getEntity(entityId);
        for (ICostThing costThing : costThings) {
            recruit.setPoints(recruit.getPoints() - costThing.getNum());
        }
        rcruitService.update(recruit);
        return new CostReceipt(costThings);
    }
    
    @Override
	protected String getOwnerName(long entityId) {
		return playerService.getEntity(entityId).getNick();
	}
}
