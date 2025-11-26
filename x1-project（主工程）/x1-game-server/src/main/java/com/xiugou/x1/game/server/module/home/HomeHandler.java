/**
 *
 */
package com.xiugou.x1.game.server.module.home;

import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.HomeProducePoolCache;
import com.xiugou.x1.design.module.HomeStoreHouseCache;
import com.xiugou.x1.design.module.NpcBuildingCache;
import com.xiugou.x1.design.module.autogen.HomeProducePoolAbstractCache.HomeProducePoolCfg;
import com.xiugou.x1.design.module.autogen.HomeStoreHouseAbstractCache.HomeStoreHouseCfg;
import com.xiugou.x1.design.module.autogen.NpcBuildingAbstractCache.NpcBuildingCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.home.constant.BuildingType;
import com.xiugou.x1.game.server.module.home.event.HomeCollectionResourceEvent;
import com.xiugou.x1.game.server.module.home.event.UnlockBuildingsEvent;
import com.xiugou.x1.game.server.module.home.model.Home;
import com.xiugou.x1.game.server.module.home.service.HomeService;
import com.xiugou.x1.game.server.module.home.struct.BuildingData;
import com.xiugou.x1.game.server.module.home.struct.HomeProducer;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.bag.Home.HomeBuildingActivateRequest;
import pb.xiugou.x1.protobuf.bag.Home.HomeBuildingActivateResponse;
import pb.xiugou.x1.protobuf.bag.Home.HomeInfoResponse;
import pb.xiugou.x1.protobuf.bag.Home.HomeProducerLevelUpRequest;
import pb.xiugou.x1.protobuf.bag.Home.HomeProducerLevelUpResponse;
import pb.xiugou.x1.protobuf.bag.Home.HomeTakeProduceRewardRequest;
import pb.xiugou.x1.protobuf.bag.Home.HomeTakeProduceRewardResponse;

/**
 * @author YY
 */
@Controller
public class HomeHandler extends AbstractModuleHandler {

    @Autowired
    private HomeService homeService;
    @Autowired
    private HomeProducePoolCache homeProducePoolCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private MainlinePlayerService mainlinePlayerService;
    @Autowired
    private HomeStoreHouseCache homeStoreHouseCache;
    @Autowired
    private NpcBuildingCache npcBuildingCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.BASE;
	}
    
    @Override
	public boolean needDailyPush() {
		return false;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        Home home = homeService.getEntity(playerId);
        
        boolean needUpdate = false;
        for(HomeProducer homeProducer : home.getProducers().values()) {
        	long oldProduct = homeProducer.getProduce();
            homeService.calculateOutput(homeProducer);
            if(oldProduct != homeProducer.getProduce()) {
            	needUpdate = true;
            }
        }
        if(needUpdate) {
        	homeService.update(home);
        }

        HomeInfoResponse.Builder response = HomeInfoResponse.newBuilder();
        response.setStoreLv(home.getStoreLv());
        ///所有建筑物
        for (BuildingData building : home.getBuildingOpening().values()) {
            response.addBuildings(homeService.build(building));
        }
        //资源池
        for (HomeProducer homeProducer : home.getProducers().values()) {
            response.addProducers(homeService.build(homeProducer));
        }
        playerContextManager.push(playerId, HomeInfoResponse.Proto.ID, response.build());
    }

    //资源池升级
    @PlayerCmd
    public HomeProducerLevelUpResponse levelUp(PlayerContext playerContext, HomeProducerLevelUpRequest request) {
        Home home = homeService.getEntity(playerContext.getId());

        HomeProducer homeProducer = home.getProducers().get(request.getBuildingId());
        Asserts.isTrue(homeProducer != null, TipsCode.HOME_PRODUCE_UNACTIVATED);

        HomeProducePoolCfg homeProducePoolCfg = homeProducePoolCache.getInBuildingIdLevelIndex(homeProducer.getId(), homeProducer.getLevel());
        Asserts.isTrue(homeProducePoolCfg.getUpCost().getThingId() > 0, TipsCode.HOME_PRODUCE_MAX_UP);
        
        thingService.cost(playerContext.getId(), homeProducePoolCfg.getUpCost(), GameCause.HOME_PRODUCE_POOL_UP_LEVEL);

        //先结算一次当前的产量
        homeService.calculateOutput(homeProducer);
        homeProducer.setLevel(homeProducer.getLevel() + 1);
        homeProducer.setStartTime(DateTimeUtil.currMillis());
        homeService.update(home);

        HomeProducerLevelUpResponse.Builder response = HomeProducerLevelUpResponse.newBuilder();
        response.setProducer(homeService.build(homeProducer));
        return response.build();
    }

    //领取资源池中的产出
    @PlayerCmd
    public HomeTakeProduceRewardResponse take(PlayerContext playerContext, HomeTakeProduceRewardRequest request) {
        Home home = homeService.getEntity(playerContext.getId());
        HomeProducer homeProducer = home.getProducers().get(request.getBuildingId());
        Asserts.isTrue(homeProducer != null, TipsCode.HOME_PRODUCE_UNACTIVATED);
        HomeProducePoolCfg homeProducePoolCfg = homeProducePoolCache.getInBuildingIdLevelIndex(homeProducer.getId(), homeProducer.getLevel());

        HomeStoreHouseCfg homeStoreHouseCfg = homeStoreHouseCache.getOrThrow(home.getStoreLv());
        long limit = 0;
        long nowHave = 0;
        if (homeProducer.getId() == 200) {
            limit = homeStoreHouseCfg.getWood();
            nowHave = home.getWood();
        } else if (homeProducer.getId() == 201) {
            limit = homeStoreHouseCfg.getMine();
            nowHave = home.getMine();
        } else if (homeProducer.getId() == 202) {
            limit = Long.MAX_VALUE;
        }

        //计算间隔时间内的产值，超出最大容量部分舍弃，并且将非整数部分保留
        homeService.calculateOutput(homeProducer);
        long produce = homeProducer.getProduce();
        if (produce <= 0) {
            return HomeTakeProduceRewardResponse.getDefaultInstance();
        }
        long canTake = 0;
        if (nowHave + produce > limit) {
            canTake = limit - nowHave;
        } else {
            canTake = produce;
        }
        //到达上限
        if (canTake <= 0) {
            return HomeTakeProduceRewardResponse.getDefaultInstance();
        }
        //玩家的资源池已经满了很久，导致资源池的产出时间一直是很旧的时间，那么当玩家再次正常领取资源时，应该把资源产出时间调整至当前时间，不然会有源源不断的资源可以领取
        if(homeProducer.getProduce() >= homeProducePoolCfg.getMaxStore()) {
        	homeProducer.setStartTime(DateTimeUtil.currMillis());
        }
        homeProducer.setProduce(produce - canTake);
        homeService.update(home);
        
        thingService.add(playerContext.getId(), RewardThing.of(homeProducePoolCfg.getProduceType(), canTake), GameCause.HOME_PRODUCE_POOL_REWARD, NoticeType.SLIENT);

        //资源收集事件
        EventBus.post(HomeCollectionResourceEvent.of(playerContext.getId(), homeProducePoolCfg.getProduceType(), canTake));

        HomeTakeProduceRewardResponse.Builder response = HomeTakeProduceRewardResponse.newBuilder();
        response.setProducer(homeService.build(homeProducer));
        return response.build();
    }

    /**
     * 激活建筑
     */
    @PlayerCmd
    public HomeBuildingActivateResponse activate(PlayerContext playerContext, HomeBuildingActivateRequest request) {
        Home home = homeService.getEntity(playerContext.getId());
        Map<Integer, BuildingData> buildingOpening = home.getBuildingOpening();

        BuildingType buildingType = BuildingType.valueOf(request.getBuildingId());
        //该建筑 是否已经激活
        BuildingData buildingData = buildingOpening.get(buildingType.getBuildingId());
        Asserts.isTrue(buildingData == null || !buildingData.isActive(), TipsCode.HOME_BUILDING_ACTIVATED);

        NpcBuildingCfg npcBuildingCfg = npcBuildingCache.getOrThrow(buildingType.getBuildingId());

        //前置条件是否已激活
        int preBuildingId = npcBuildingCfg.getPreBuildingId();
        if (preBuildingId != 0) {
            BuildingType preBuildingType = BuildingType.valueOf(preBuildingId);
            Asserts.isTrue(home.isBuildingOpen(preBuildingType), TipsCode.HOME_PRODUCE_NOT_OPEN_PRECONDITION);
        }
        if (buildingData == null) {
            buildingData = new BuildingData();
            buildingData.setActive(false);
            buildingData.setId(buildingType.getBuildingId());
            buildingOpening.put(buildingData.getId(), buildingData);
        }

        //消耗 激活建筑所需道具
        boolean flag = false;
        List<CostThing> costThing = npcBuildingCfg.getCostThing();
        if (!costThing.isEmpty()) {
            flag = mainlinePlayerService.checkSceneOpening(playerContext.getId(), buildingData.getOpening(), costThing, GameCause.HOME_PRODUCE_ACTIVATE);
        } else {
            flag = true;
        }
        HomeProducer homeProducer = null;
        if (flag) {
            buildingData.setActive(true);
            if (npcBuildingCfg.getType() == 2) {
                homeProducer = new HomeProducer();
                homeProducer.setId(npcBuildingCfg.getId());
                homeProducer.setLevel(1);
                homeProducer.setStartTime(DateTimeUtil.currMillis());
                HomeProducePoolCfg homeProducePoolCfg = homeProducePoolCache.getInBuildingIdLevelIndex(npcBuildingCfg.getId(), homeProducer.getLevel());
                homeProducer.setProduce(homeProducePoolCfg.getMaxStore());
                home.getProducers().put(npcBuildingCfg.getId(), homeProducer);
            } else if (npcBuildingCfg.getType() == 3) {
                home.setStoreLv(home.getStoreLv() + 1);
            }
            EventBus.post(UnlockBuildingsEvent.of(playerContext.getId(), buildingType.getBuildingId()));
        }
        homeService.update(home);

        HomeBuildingActivateResponse.Builder response = HomeBuildingActivateResponse.newBuilder();
        response.setBuilding(homeService.build(buildingData));
        response.setStoreLv(home.getStoreLv());
        if (homeProducer != null) {
            response.setProducer(homeService.build(homeProducer));
        }
        return response.build();
    }
}
