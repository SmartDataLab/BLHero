package com.xiugou.x1.game.server.module.player.service;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.ExpRewardReceipt;
import org.gaming.prefab.thing.ExpThingStorer;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.VipCache;
import com.xiugou.x1.design.module.autogen.VipAbstractCache.VipCfg;
import com.xiugou.x1.game.server.module.player.event.PlayerVipUpLevelEvent;
import com.xiugou.x1.game.server.module.player.model.Player;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbPlayerVipExp;
import pojo.xiugou.x1.pojo.log.player.PlayerVipLog;

/**
 * @author yh
 * @date 2023/8/24
 * @apiNote
 */
@Component
public class PlayerVipExpStorer extends ExpThingStorer<PlayerVipLog> {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private VipCache vipCache;

    @Override
    protected PlayerVipLog newLog() {
        return new PlayerVipLog();
    }

    @Override
    protected IThingType thingType() {
        return ItemType.VIP;
    }

    @Override
    protected ITipCause lackCode() {
        throw new UnsupportedOperationException("不应该被调用的函数");
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return playerService.getEntity(entityId).getVipExp();
    }

    @Override
    protected ExpRewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        long total = 0;
        for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
        Player player = playerService.getEntity(entityId);
        int oldLevel = player.getVipLevel();
        player.setVipExp(player.getVipExp() + total);

        VipCfg vipCfg = vipCache.getOrNull(player.getVipLevel());
        boolean levelUp = false;
        while (vipCfg != null && vipCfg.getNeedExp() > 0 && player.getVipExp() >= vipCfg.getNeedExp()) {
            player.setVipLevel(player.getVipLevel() + 1);
            player.setVipExp(player.getVipExp() - vipCfg.getNeedExp());
            vipCfg = vipCache.getOrNull(player.getVipLevel());
            levelUp = true;
        }
        
        playerService.update(player);
        
        if (levelUp) {
            PbPlayerVipExp.Builder builder = PbPlayerVipExp.newBuilder();
            builder.setOldLevel(oldLevel);
            builder.setNewLevel(player.getVipLevel());
            builder.setCurrExp(player.getVipExp());

            EventBus.post(PlayerVipUpLevelEvent.of(player));

            ExpRewardReceipt receipt = new ExpRewardReceipt();
            receipt.setOldLevel(oldLevel);
            receipt.setCurrLevel(player.getVipLevel());
            receipt.append(new RewardReceipt.RewardDetail(thingType().getThingId(), total, builder.build().toByteString()));
            return receipt;
        } else {
            return new ExpRewardReceipt(rewardThings, oldLevel, player.getVipLevel());
        }
    }

    @Override
    protected BaseRepository<PlayerVipLog> initRepository() {
        return SlimDao.getRepository(PlayerVipLog.class);
    }

    @Override
    protected String getOwnerName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }
}
