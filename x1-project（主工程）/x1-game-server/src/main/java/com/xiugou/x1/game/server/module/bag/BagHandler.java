package com.xiugou.x1.game.server.module.bag;

import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.ThingStorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.Common;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemClass;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.constant.UseItemType;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.QuickBuyingCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.module.autogen.QuickBuyingAbstractCache.QuickBuyingCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.message.GodFingerMessage;
import com.xiugou.x1.game.server.module.bag.model.Bag;
import com.xiugou.x1.game.server.module.bag.service.AbsUseItemService;
import com.xiugou.x1.game.server.module.bag.service.BagService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.service.EquipService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.bag.Bag.BagInfoResponse;
import pb.xiugou.x1.protobuf.bag.Bag.BagUseItemRequest;
import pb.xiugou.x1.protobuf.bag.Bag.BagUseItemResponse;
import pb.xiugou.x1.protobuf.bag.Bag.QuickBuyingRequest;
import pb.xiugou.x1.protobuf.bag.Bag.QuickBuyingResponse;
import pb.xiugou.x1.protobuf.equip.Equip.PbEquip;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbItem;

/**
 * @author yh
 * @date 2023/5/30
 * @apiNote
 */
@Controller
public class BagHandler extends AbstractModuleHandler {

    private static Logger logger = LoggerFactory.getLogger(BagHandler.class);

    @Autowired
    private BagService bagService;
    @Autowired
    private ThingService thingService;
    @Autowired
    private EquipService equipService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private QuickBuyingCache quickBuyingCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
	public boolean needDailyPush() {
		return false;
	}
    
    @Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        Bag entity = bagService.getEntity(playerId);
        BagInfoResponse.Builder response = BagInfoResponse.newBuilder();
        //道具类
        Map<Integer, ItemGrid> idGridMap = entity.getIdGridMap();
        for (ItemGrid grid : idGridMap.values()) {
            PbItem.Builder data = PbItem.newBuilder();
            data.setItem(grid.getItemId());
            data.setNum(grid.getNum());
            response.addGrids(data);
        }
        //资源类
        for (ItemType itemType : ItemType.values()) {
            if (itemType.getItemClass() != ItemClass.NUMBER) {
                continue;
            }
            ThingStorer<?, ?> storer = thingService.getStorer(itemType);
            long num = storer.getCount(playerId, 0);

            PbItem.Builder data = PbItem.newBuilder();
            data.setItem(itemType.getThingId());
            data.setNum(num);
            response.addResources(data.build());
        }
        //装备
        List<Equip> equipList = equipService.getEntities(playerId);
        for (Equip equip : equipList) {
            PbEquip build = equipService.build(equip);
            response.addEquips(build);
        }
        playerContextManager.push(playerId, BagInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public BagUseItemResponse useItem(PlayerContext playerContext, BagUseItemRequest request) {
        ItemCfg itemCfg = itemCache.getOrThrow(request.getItemId());

        AbsUseItemService service = AbsUseItemService.getService(UseItemType.valueOf(itemCfg.getUseType()));
        Asserts.isTrue(service != null, TipsCode.BAG_ITEM_CANT_USE);
        
        service.use(playerContext.getId(), request.getItemId(), request.getNum(), request.getOption());
        return BagUseItemResponse.getDefaultInstance();
    }

    @InternalCmd
    public void godFinger(GodFingerMessage message) {
        if (playerService.isPlayer(message.getPlayerId())) {
            thingService.add(message.getPlayerId(), RewardThing.of(Common.VOUCHER, message.getMoney()), GameCause.GOD_FINGER);
        } else {
            logger.error("未找到服务器下ID为{}的玩家数据", message.getPlayerId());
        }
    }
    
    @PlayerCmd
    public QuickBuyingResponse quickBuying(PlayerContext playerContext, QuickBuyingRequest request) {
    	Asserts.isTrue(request.getNum() > 0, TipsCode.ERROR_PARAM);
    	QuickBuyingCfg quickBuyingCfg = quickBuyingCache.getOrThrow(request.getItem());
    	
    	int multiple = (int)Math.ceil(request.getNum() * 1.0f / quickBuyingCfg.getNum());
    	CostThing costThing = ThingUtil.multiplyCost(quickBuyingCfg.getCost(), multiple);
    	thingService.cost(playerContext.getId(), costThing, GameCause.BAG_QUICK_BUY);
    	
    	RewardThing rewardThing = RewardThing.of(request.getItem(), quickBuyingCfg.getNum() * multiple);
    	thingService.add(playerContext.getId(), rewardThing, GameCause.BAG_QUICK_BUY);
    	
    	return QuickBuyingResponse.getDefaultInstance();
    }
}
