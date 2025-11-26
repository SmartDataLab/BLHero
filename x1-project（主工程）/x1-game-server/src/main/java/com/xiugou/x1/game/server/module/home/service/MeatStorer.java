/**
 *
 */
package com.xiugou.x1.game.server.module.home.service;

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
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.HomeStoreHouseCache;
import com.xiugou.x1.design.module.autogen.HomeStoreHouseAbstractCache.HomeStoreHouseCfg;
import com.xiugou.x1.game.server.module.home.event.MeatChangeEvent;
import com.xiugou.x1.game.server.module.home.model.Home;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.home.MeatLog;

/**
 * @author YY
 *
 */
@Component
public class MeatStorer extends NumberThingStorer<MeatLog> {

    @Autowired
    private HomeService homeService;
    @Autowired
    private HomeStoreHouseCache homeStoreHouseCache;
    @Autowired
    private PlayerService playerService;
    @Autowired
	private BattleConstCache battleConstCache;
	@Autowired
	private MainlinePlayerService mainlinePlayerService;

    @Override
    protected MeatLog newLog() {
        return new MeatLog();
    }

    @Override
    protected BaseRepository<MeatLog> initRepository() {
        return SlimDao.getRepository(MeatLog.class);
    }

    @Override
    protected IThingType thingType() {
        return ItemType.MEAT;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.MEAT_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        return homeService.getEntity(entityId).getMeat();
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        Home home = homeService.getEntity(entityId);
        long total = 0;
        for (IRewardThing thing : rewardThings) {
            total += thing.getNum();
        }
        long overflow = 0;
        if(!cause.isOverLimit()) {
        	HomeStoreHouseCfg homeStoreHouseCfg = homeStoreHouseCache.getOrThrow(home.getStoreLv());
        	MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(entityId);
			//高级特权增加的露营存储量上限
			long addStoreLimit = 0;
			if(mainlinePlayer.isCampingAdv()) {
				addStoreLimit = battleConstCache.getCamp_adv_store_up();
			}
        	if (home.getMeat() >= homeStoreHouseCfg.getMeat() + addStoreLimit) {
                return RewardReceipt.EMPTY;
            }
        	if (total > homeStoreHouseCfg.getMeat() - home.getMeat()) {
                overflow = total + home.getMeat() - homeStoreHouseCfg.getMeat();
                total = homeStoreHouseCfg.getMeat() - home.getMeat();
            }
        }
        home.setMeat(home.getMeat() + total);
        homeService.update(home);
        RewardReceipt rewardReceipt = new RewardReceipt();
        if (overflow > 0) {
            rewardReceipt.setRemark("溢出" + overflow);
        }
        rewardReceipt.append(new RewardDetail(ItemType.MEAT.getThingId(), total));

        EventBus.post(MeatChangeEvent.of(entityId, home.getMeat()));
        return rewardReceipt;
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        Home home = homeService.getEntity(entityId);
        for (ICostThing thing : costThings) {
            home.setMeat(home.getMeat() - thing.getNum());
        }
        homeService.update(home);
        EventBus.post(MeatChangeEvent.of(entityId, home.getMeat()));
        return new CostReceipt(costThings);
    }

    @Override
	protected String getOwnerName(long entityId) {
		return playerService.getEntity(entityId).getNick();
	}
}
