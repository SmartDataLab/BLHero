package com.xiugou.x1.game.server.module.bag.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.game.server.module.bag.model.Bag;
import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.bag.ItemLog;


/**
 * @author yh
 * @date 2023/5/29
 * @apiNote
 */
@Component
public class ItemStorer extends NumberThingStorer<ItemLog> {
	
    @Autowired
    private BagService bagService;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private PlayerService playerService;

    @Override
    protected IThingType thingType() {
        return ItemType.ITEM;
    }

    @Override
    protected ITipCause lackCode() {
        return TipsCode.BAG_ITEM_LACK;
    }

    @Override
    public long getCount(long entityId, int thingId) {
        Map<Integer, ItemGrid> idGridMap = bagService.getEntity(entityId).getIdGridMap();
        ItemGrid itemGrid = idGridMap.get(thingId);
        if (itemGrid == null) {
            return 0;
        } else {
            return itemGrid.getNum();
        }
    }

    @Override
    protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
        Bag bagEntity = bagService.getEntity(entityId);
        Map<Integer, ItemGrid> idGridMap = bagEntity.getIdGridMap();//玩家道具背包信息
        for (IRewardThing reward : rewardThings) {
            int thingId = reward.getThingId();    //道具ID
            long num = reward.getNum();  //数量
            ItemGrid itemGrid = idGridMap.get(thingId);
            if (itemGrid == null) {  //为空则说明 玩家没有该道具格子 需要新构建一个
                itemGrid = new ItemGrid();
                itemGrid.setItemId(thingId);
                itemGrid.setNum(num);
                idGridMap.put(thingId, itemGrid);
            } else {
                itemGrid.setNum(itemGrid.getNum() + num);
            }
        }
        bagService.update(bagEntity);
        return new RewardReceipt(rewardThings);
    }

    @Override
    protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
        Bag entity = bagService.getEntity(entityId);
        Map<Integer, ItemGrid> idGridMap = entity.getIdGridMap();
        for (ICostThing cost : costThings) {
            ItemGrid itemGrid = idGridMap.get(cost.getThingId());
            itemGrid.setNum(itemGrid.getNum() - cost.getNum());
            if(itemGrid.getNum() <= 0) {
            	idGridMap.remove(itemGrid.getItemId());
            }
        }
        bagService.update(entity);
        return new CostReceipt(costThings);
    }

	@Override
	protected ItemLog newLog() {
		return new ItemLog();
	}

	@Override
	protected String thingName(int thingId) {
		return itemCache.getOrThrow(thingId).getName();
	}

	@Override
	protected BaseRepository<ItemLog> initRepository() {
		return SlimDao.getRepository(ItemLog.class);
	}

	@Override
	protected String getOwnerName(long entityId) {
		return playerService.getEntity(entityId).getNick();
	}
    
}
