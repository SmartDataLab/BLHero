/**
 *
 */
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
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.PlayerLevelCache;
import com.xiugou.x1.design.module.autogen.PlayerLevelAbstractCache.PlayerLevelCfg;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.player.event.PlayerUpLevelEvent;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.vip.constant.VipFuncType;
import com.xiugou.x1.game.server.module.vip.service.VipService;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbPlayerExp;
import pojo.xiugou.x1.pojo.log.player.PlayerExpLog;

/**
 * @author YY
 *
 */
@Component
public class PlayerExpStorer extends ExpThingStorer<PlayerExpLog> {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerLevelCache playerLevelCache;
    @Autowired
    private OpenFunctionService openFunctionService;
    @Autowired
    private VipService vipService;

    @Override
    protected PlayerExpLog newLog() {
        return new PlayerExpLog();
    }

    @Override
    protected IThingType thingType() {
        return ItemType.EXP;
    }

    @Override
    protected ITipCause lackCode() {
        throw new UnsupportedOperationException("不应该被调用的函数");
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return playerService.getEntity(entityId).getExp();
    }

    @Override
    protected ExpRewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        long total = 0;
        for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
        //VIP特权加成比例
        float boostRate = vipService.boostRate(entityId, VipFuncType.MAINLINE_EXP_RATE, cause.getCode());
        total = (long)(total * (1 + boostRate));
        
        Player player = playerService.getEntity(entityId);
        int oldLevel = player.getLevel();
        player.setExp(player.getExp() + total);

        PlayerLevelCfg playerLevelCfg = playerLevelCache.getOrNull(player.getLevel());
        boolean levelUp = false;
        while (playerLevelCfg != null && player.getExp() >= playerLevelCfg.getExp() && playerLevelCfg.getExp() > 0) {
            player.setLevel(player.getLevel() + 1);
            player.setExp(player.getExp() - playerLevelCfg.getExp());
            playerLevelCfg = playerLevelCache.getOrNull(player.getLevel());
            levelUp = true;
        }
        
        playerService.update(player);
        
        if (levelUp) {
            PbPlayerExp.Builder builder = PbPlayerExp.newBuilder();
            builder.setOldLevel(oldLevel);
            builder.setNewLevel(player.getLevel());
            builder.setCurrExp(player.getExp());
            List<Integer> newFunctionIds = openFunctionService.checkAndGetNewFunctions(entityId);
            newFunctionIds = openFunctionService.filterFunctionByDevice(player.getLoginDeviceType(), newFunctionIds);
            builder.addAllOpenFunctions(newFunctionIds);

            EventBus.post(PlayerUpLevelEvent.of(player));
            playerService.addToReport(player);

            ExpRewardReceipt receipt = new ExpRewardReceipt();
            receipt.setOldLevel(oldLevel);
            receipt.setCurrLevel(player.getLevel());
            receipt.append(new RewardDetail(thingType().getThingId(), total, builder.build().toByteString()));
            return receipt;
        } else {
        	ExpRewardReceipt receipt = new ExpRewardReceipt();
        	receipt.setOldLevel(oldLevel);
            receipt.setCurrLevel(player.getLevel());
            receipt.append(new RewardDetail(thingType().getThingId(), total, null));
            return receipt;
        }
    }

    @Override
    protected BaseRepository<PlayerExpLog> initRepository() {
        return SlimDao.getRepository(PlayerExpLog.class);
    }

    @Override
    protected String getOwnerName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }
}
