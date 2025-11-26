package com.xiugou.x1.game.server.module.evil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.module.evil.model.Evil;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.refineEvil.EvilCatalogLog;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Component
public class EvilStorer extends NumberThingStorer<EvilCatalogLog> {
    @Autowired
    private EvilService evilService;
    @Autowired
    private HeroService heroService;
    @Autowired
    private PlayerService playerService;
    
    @Override
    protected EvilCatalogLog newLog() {
        return new EvilCatalogLog();
    }

    @Override
    protected BaseRepository<EvilCatalogLog> initRepository() {
        return SlimDao.getRepository(EvilCatalogLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.EVIL_CATALOG;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.EVI_FRAGMENT_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        Evil entity = evilService.getEntity(entityId, thingId);
        if (entity == null) {
            return 0;
        } else {
            return entity.getFragment();
        }
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        Map<Integer, Evil> evilMap = evilService.getEvilMap(entityId);
        List<Evil> insertList = new ArrayList<>();
        List<Evil> updateList = new ArrayList<>();
        
        RewardReceipt receipt = new RewardReceipt();
        
        for (IRewardThing reward : rewardThings) {
            Evil evil = evilMap.get(reward.getThingId());
            RewardDetail rewardDetail = null;
            if (evil == null) {
                evil = new Evil();
                evil.setPid(entityId);
                evil.setLevel(1);
                evil.setIdentity(reward.getThingId());
                evil.setFragment(reward.getNum() - 1);
                insertList.add(evil);
                
                rewardDetail = new RewardDetail(reward.getThingId(), reward.getNum(), evilService.build(evil).toByteString());
            } else {
                evil.setFragment(evil.getFragment() + reward.getNum());
                updateList.add(evil);
                
                rewardDetail = new RewardDetail(reward.getThingId(), reward.getNum(), evilService.build(evil).toByteString());
            }
            receipt.append(rewardDetail);
        }
        evilService.update(updateList);
		if (!insertList.isEmpty()) {
			evilService.insert(insertList);
			heroService.calculateAllHeroAttr(entityId, cause);
		}
        return receipt;
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        Map<Integer, Evil> evilMap = evilService.getEvilMap(entityId);
        List<Evil> updateList = new ArrayList<>();
        for (ICostThing costThing : costThings) {
            Evil evil = evilMap.get(costThing.getThingId());
            evil.setFragment(evil.getFragment() - costThing.getNum());
            updateList.add(evil);
        }
        evilService.update(updateList);
        return new CostReceipt(costThings);
    }
    
    @Override
    protected String getOwnerName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }
}
