/**
 *
 */
package com.xiugou.x1.game.server.module.bag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemClass;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.EquipCache;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.EquipAbstractCache.EquipCfg;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.model.Bag;
import com.xiugou.x1.game.server.module.bag.service.BagService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import com.xiugou.x1.game.server.module.equip.EquipHandler;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.model.EquipWear;
import com.xiugou.x1.game.server.module.equip.service.EquipService;
import com.xiugou.x1.game.server.module.equip.service.EquipWearService;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.home.HomeHandler;

import pb.xiugou.x1.protobuf.equip.Equip.EquipGmDeleteMessage;

/**
 * @author YY
 */
@Controller
public class BagGmHandler {

    @Autowired
    private ThingService thingService;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private BagService bagService;
    @Autowired
    private EquipCache equipCache;
    @Autowired
    private EquipService equipService;
    @Autowired
    private EquipWearService equipWearService;
    @Autowired
    private HeroService heroService;
    @Autowired
    private BagHandler bagHandler;
    @Autowired
    private HomeHandler homeHandler;
    @Autowired
    private EquipHandler equipHandler;
    @Autowired
    private PlayerContextManager playerContextManager;

    @PlayerGmCmd(command = "ADD_ITEM")
    public void addItem(PlayerContext playerContext, String[] params) {
        Asserts.isTrue(params.length == 2, TipsCode.GM_PARAME_EMPTY);  //参数必须为两个（道具ID：数量）

        int itemId = Integer.parseInt(params[0]);
        long num = Integer.parseInt(params[1]);
        ItemCfg itemCfg = itemCache.getOrThrow(itemId);
        ItemType itemType = ItemType.valueOf(itemCfg.getKind());
        ItemClass itemClass = itemType.getItemClass();

        if (itemClass == ItemClass.ITEM || itemClass == ItemClass.NUMBER || itemClass == ItemClass.EXP) {
        } else {
            if (num > 1000) {
                num = 1000;
            }
        }
        thingService.add(playerContext.getId(), RewardThing.of(itemId, num), GameCause.GM);
    }

    @PlayerGmCmd(command = "ADD_RES")
    public void addResource(PlayerContext playerContext, String[] params) {
        ArrayList<RewardThing> rewardThings = new ArrayList<>();
        rewardThings.add(RewardThing.of(4, 1250));
        rewardThings.add(RewardThing.of(5, 1250));
        rewardThings.add(RewardThing.of(6, 1250));
        thingService.add(playerContext.getId(), rewardThings, GameCause.GM);
    }

    @PlayerGmCmd(command = "COST_ITEM")
    public void clearResource(PlayerContext playerContext, String[] params) {
        int itemId = Integer.parseInt(params[0]);
        int num = Integer.parseInt(params[1]);

        try {
            if (thingService.isEnough(playerContext.getId(), CostThing.of(itemId, num))) {
                thingService.cost(playerContext.getId(), CostThing.of(itemId, num), GameCause.GM);
            } else {
                ItemCfg itemCfg = itemCache.getOrThrow(itemId);
                ItemType itemType = ItemType.valueOf(itemCfg.getKind());
                long hasCount = thingService.getStorer(itemType).getCount(playerContext.getId(), itemId);
                thingService.cost(playerContext.getId(), CostThing.of(itemId, hasCount), GameCause.GM);
            }
        } catch (Exception e) {
            //不能被扣除的东西
            e.printStackTrace();
        }
        bagHandler.pushInfo(playerContext);

    }

    @PlayerGmCmd(command = "ALL_ITEM")
    public void addAllItem(PlayerContext playerContext, String[] params) {
        List<ItemCfg> all = itemCache.all();
        List<RewardThing> rewards = new ArrayList<>();
        for (ItemCfg item : all) {
            if (item.getKind() != ItemType.ITEM.getType()) {
                continue;
            }
            rewards.add(RewardThing.of(item.getId(), 100));
        }
        thingService.add(playerContext.getId(), rewards, GameCause.GM);
        bagHandler.pushInfo(playerContext);

    }

    @PlayerGmCmd(command = "ALL_EQUIP")
    public void allEquip(PlayerContext playerContext, String[] params) {
        List<EquipCfg> all = equipCache.all();
        ArrayList<RewardThing> rewards = new ArrayList<>();
        for (EquipCfg equipCfg : all) {
            rewards.add(RewardThing.of(equipCfg.getEquipId(), 1));
        }
        thingService.add(playerContext.getId(), rewards, GameCause.GM);
        equipHandler.pushInfo(playerContext);
    }

    /**
     * 扣除所有道具
     */
    @PlayerGmCmd(command = "COST_ALL_ITEM")
    public void costAllItem(PlayerContext playerContext, String[] params) {
        Bag bag = bagService.getEntity(playerContext.getId());
        Map<Integer, ItemGrid> idGridMap = bag.getIdGridMap();
        ArrayList<CostThing> costs = new ArrayList<>();
        for (ItemGrid entity : idGridMap.values()) {
            costs.add(CostThing.of(entity.getItemId(), entity.getNum()));
        }
        thingService.cost(playerContext.getId(), costs, GameCause.GM);
        bagHandler.pushInfo(playerContext);

    }

    @PlayerGmCmd(command = "COST_ALL_EQUIP")
    public void costAllEquip(PlayerContext playerContext, String[] params) {
    	List<Equip> equips = equipService.getEntities(playerContext.getId());
    	
    	List<Equip> deleteList = new ArrayList<>();
    	for(Equip equip : equips) {
    		if(equip.isWear()) {
    			continue;
    		}
    		deleteList.add(equip);
    	}
        equipService.deleteAll(playerContext.getId(), deleteList, GameCause.GM);
        equipHandler.pushInfo(playerContext);
        
        EquipGmDeleteMessage.Builder builder = EquipGmDeleteMessage.newBuilder();
        for(Equip equip : deleteList) {
        	builder.addEquips(equip.getId());
        }
        playerContextManager.push(playerContext.getId(), EquipGmDeleteMessage.Proto.ID, builder.build());
    }

    @PlayerGmCmd(command = "TAKE_OFF_EQUIP")
    public void takeOffeEquip(PlayerContext playerContext, String[] params) {
        EquipWear entity = equipWearService.getEntity(playerContext.getId());
        Map<Integer, Long> wearing = entity.getWearing();
        Collection<Long> values = wearing.values();
        List<Equip> equipList = equipService.getEntities(playerContext.getId());
        List<Equip> collect = equipList.stream().filter(e -> values.contains(e.getId())).collect(Collectors.toList());
        wearing.clear();
        equipWearService.update(entity);
        for (Equip equip : collect) {
            equip.setWear(false);
        }
        equipService.update(equipList);
        heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.EQUIP_CHANGE); //计算属性
        equipHandler.pushInfo(playerContext);

    }

    @PlayerGmCmd(command = "BAG_INFO")
    public void bagInfo(PlayerContext playerContext, String[] params) {
        bagHandler.pushInfo(playerContext);
    }

    @PlayerGmCmd(command = "ADD_PROP")
    public void addProp(PlayerContext playerContext, String[] params) {
        ArrayList<RewardThing> rewardThings = new ArrayList<>();
        rewardThings.add(RewardThing.of(1, 10000));
        rewardThings.add(RewardThing.of(2, 10000));
        rewardThings.add(RewardThing.of(3, 10000000));
        rewardThings.add(RewardThing.of(4, 1250));
        rewardThings.add(RewardThing.of(5, 1250));
        rewardThings.add(RewardThing.of(6, 1250));
        thingService.add(playerContext.getId(), rewardThings, GameCause.GM);
    }

    @PlayerGmCmd(command = "HOME_HANDLER_INFO")
    public void homeHandlerInfo(PlayerContext playerContext, String[] params) {
        homeHandler.pushInfo(playerContext);
    }
}
