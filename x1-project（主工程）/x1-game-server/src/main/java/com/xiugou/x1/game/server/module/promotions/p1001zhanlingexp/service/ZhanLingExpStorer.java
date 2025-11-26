/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.ExpRewardReceipt;
import org.gaming.prefab.thing.ExpThingStorer;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.ZhanLingExpCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingExpAbstractCache.ZhanLingExpCfg;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model.ZhanLingExp;

import pojo.xiugou.x1.pojo.log.promotions.ZhanLingExpLog;

/**
 * @author hyy
 *
 */
@Component
public class ZhanLingExpStorer extends ExpThingStorer<ZhanLingExpLog> {

	@Autowired
    private ItemCache itemCache;
	@Autowired
	private ZhanLingExpService zhanLingExpService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ZhanLingExpCache zhanLingExpCache;
	
	@Override
	protected ZhanLingExpLog newLog() {
		return new ZhanLingExpLog();
	}

	@Override
	protected BaseRepository<ZhanLingExpLog> initRepository() {
		return SlimDao.getRepository(ZhanLingExpLog.class);
	}

	@Override
	protected IThingType thingType() {
		return ItemType.ZHAN_LING_EXP;
	}

	@Override
	protected ITipCause lackCode() {
        throw new UnsupportedOperationException("不应该被调用的函数");
	}

	@Override
	public long getCount(long entityId, int thingId) {
		ItemCfg itemCfg = itemCache.getOrThrow(thingId);
		ZhanLingExp entity = zhanLingExpService.getEntity(entityId, itemCfg.getKindParam());
        return entity.getExp();
	}

	@Override
	protected ExpRewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause,
			String remark) {
		for(IRewardThing rewardThing : rewardThings) {
			ItemCfg itemCfg = itemCache.getOrThrow(rewardThing.getThingId());
			if(!promotionControlService.isRunning(applicationSettings.getGameServerId(), itemCfg.getKindParam())) {
				continue;
			}
			ZhanLingExp entity = zhanLingExpService.getEntity(entityId, itemCfg.getKindParam());
			entity.setExp(entity.getExp() + rewardThing.getNum());
			
	        ZhanLingExpCfg zhanLingExpCfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), entity.getLevel() + 1);
	        while(zhanLingExpCfg != null && entity.getExp() >= zhanLingExpCfg.getNeedExp()) {
	        	entity.setExp(entity.getExp() - zhanLingExpCfg.getNeedExp());
	        	entity.setLevel(entity.getLevel() + 1);
	        	
	        	zhanLingExpCfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), entity.getLevel() + 1);
	        }
	        zhanLingExpService.update(entity);
		}
		return new ExpRewardReceipt(rewardThings, 0, 0);
	}

	@Override
	protected String getOwnerName(long entityId) {
		return "";
	}
}
