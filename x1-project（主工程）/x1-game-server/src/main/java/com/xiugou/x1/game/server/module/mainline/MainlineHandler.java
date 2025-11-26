/**
 *
 */
package com.xiugou.x1.game.server.module.mainline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskStatus;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.sprite.HarvestThing;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.ExpLimitCache;
import com.xiugou.x1.design.module.HarvestCache;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.MainlineTaskCache;
import com.xiugou.x1.design.module.MainlineTreasureBoxCache;
import com.xiugou.x1.design.module.MainlineTreasureBoxCache.MlTreasureBoxType;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.OfflineBenefitsCache;
import com.xiugou.x1.design.module.SceneFogCache;
import com.xiugou.x1.design.module.SceneNpcCache;
import com.xiugou.x1.design.module.SceneTeleportCache;
import com.xiugou.x1.design.module.autogen.ExpLimitAbstractCache.ExpLimitCfg;
import com.xiugou.x1.design.module.autogen.HarvestAbstractCache.HarvestCfg;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache.MainlineTaskCfg;
import com.xiugou.x1.design.module.autogen.MainlineTreasureBoxAbstractCache.MainlineTreasureBoxCfg;
import com.xiugou.x1.design.module.autogen.MonsterAbstractCache.MonsterCfg;
import com.xiugou.x1.design.module.autogen.OfflineBenefitsAbstractCache.OfflineBenefitsCfg;
import com.xiugou.x1.design.module.autogen.SceneFogAbstractCache.SceneFogCfg;
import com.xiugou.x1.design.module.autogen.SceneNpcAbstractCache.SceneNpcCfg;
import com.xiugou.x1.design.module.autogen.SceneTeleportAbstractCache.SceneTeleportCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.home.event.HomeCollectionResourceEvent;
import com.xiugou.x1.game.server.module.mainline.event.BuildingTransmitEvent;
import com.xiugou.x1.game.server.module.mainline.event.CampSetupEvent;
import com.xiugou.x1.game.server.module.mainline.event.CampTimeEvent;
import com.xiugou.x1.game.server.module.mainline.event.FogOpenEvent;
import com.xiugou.x1.game.server.module.mainline.event.MainlineCampAdvEvent;
import com.xiugou.x1.game.server.module.mainline.event.MainlineOpenBoxEvent;
import com.xiugou.x1.game.server.module.mainline.event.NpcInteractionEvent;
import com.xiugou.x1.game.server.module.mainline.log.MainlineDotLogger;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;
import com.xiugou.x1.game.server.module.mainline.service.MainlineSceneService;
import com.xiugou.x1.game.server.module.mainline.service.MainlineTaskSystem;
import com.xiugou.x1.game.server.module.mainline.struct.CampProduce;
import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;
import com.xiugou.x1.game.server.module.mainline.struct.TreasureBox;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineBoxInfoRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineBoxInfoResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineChallengeBossRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineChallengeBossResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineEndCampRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineEndCampResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineFogOpenRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineFogOpenResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineHarvestRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineHarvestResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineInfoResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineKillMonsterNumRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineKillMonsterNumResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineNpcOpenRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineNpcOpenResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineOpenCampAdvRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineOpenCampAdvResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineRefreshBoxRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineRefreshBoxResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineSetPortalRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineSetPortalResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineStartCampRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineStartCampResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTakeBoxRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTakeBoxResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTakeHangRewardRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTakeHangRewardResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTaskRewardRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTaskRewardResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTeleportOpenRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTeleportOpenResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTimeBoxFarawayRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTimeBoxFarawayResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineUsePortalRequest;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineUsePortalResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.PbTreasureBox;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;
import pojo.xiugou.x1.pojo.log.mainline.MainlineBossTiming;
import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskTiming;

/**
 * @author YY
 */
@Controller
public class MainlineHandler extends AbstractModuleHandler {

    @Autowired
    private MainlineSceneService mainlineSceneService;
    @Autowired
    private MainlinePlayerService mainlinePlayerService;
    @Autowired
    private HarvestCache harvestCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private MainlineTaskCache mainlineTaskCache;
    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private SceneNpcCache sceneNpcCache;
    @Autowired
    private SceneTeleportCache sceneTeleportCache;
    @Autowired
    private SceneFogCache sceneFogCache;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private FormationService formationService;
    @Autowired
    private MainlineTaskSystem mainlineTaskSystem;
    @Autowired
    private OpenFunctionService openFunctionService;
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private MainlineDotLogger mainlineDotLogger;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private MonsterCache monsterCache;
    @Autowired
    private OfflineBenefitsCache offlineBenefitsCache;
    @Autowired
    private ExpLimitCache expLimitCache;
    @Autowired
    private MainlineTreasureBoxCache mainlineTreasureBoxCache;
    @Autowired
    private ItemRandomPackCache itemRandomPackCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.BASE;
	}
    
    @Override
    public boolean needDailyPush() {
        return true;
    }

    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerId);
        mainlinePlayer.setNextBoxTime(LocalDateTimeUtil.now().plusSeconds(battleConstCache.getMainline_box_time()));
        List<MainlineScene> mainlineScenes = mainlineSceneService.getEntities(playerId);

        MainlineInfoResponse.Builder response = MainlineInfoResponse.newBuilder();
        for (MainlineScene mainlineScene : mainlineScenes) {
            response.addScenes(MainlineSceneService.build(mainlineScene));
        }
        response.setCampAdvNum(mainlinePlayer.getCampAdvNum());
        response.setNextBoxTime(LocalDateTimeUtil.toEpochMilli(mainlinePlayer.getNextBoxTime()));
        response.setTask(PbHelper.build(mainlinePlayer.getTask()));
        playerContextManager.push(playerId, MainlineInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public MainlineFogOpenResponse fogOpen(PlayerContext playerContext, MainlineFogOpenRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);

        int fogId = request.getFogId();
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), context.getSceneId());
        Asserts.isTrue(!mainlineScene.getFogs().contains(fogId), TipsCode.FOG_OPENED);

        Player player = playerService.getEntity(playerContext.getId());
        
        SceneFogCfg sceneFogCfg = sceneFogCache.getInMainlineIdFogIdIndex(context.getSceneId(), fogId);
        Asserts.isTrue(player.getLevel() >= sceneFogCfg.getOpenLevel(), TipsCode.FOG_LEVEL_LIMIT);
        
        List<CostThing> configCosts = sceneFogCfg.getOpenCosts();

        SceneOpening sceneOpening = mainlineScene.getOpeningFog(request.getFogId());
        boolean canOpen = mainlinePlayerService.checkSceneOpening(playerContext.getId(), sceneOpening, configCosts,
                GameCause.MAINLINE_FOG_OPEN);
        if (canOpen) {
            mainlineScene.getOpeningFogs().remove(fogId);
            mainlineScene.getFogs().add(fogId);
            
            EventBus.post(FogOpenEvent.of(playerContext.getId(), mainlineScene.getIdentity(), fogId));
        }
        mainlineSceneService.update(mainlineScene);
        mainlineDotLogger.addFogDot(playerContext.getId(), player.getFighting(), sceneFogCfg);

        MainlineFogOpenResponse.Builder response = MainlineFogOpenResponse.newBuilder();
        if (canOpen) {
            response.setUnlockFog(fogId);
        } else {
            response.setOpeningFog(PbHelper.build(sceneOpening));
        }
        return response.build();
    }


    @PlayerCmd
    public MainlineTeleportOpenResponse teleportOpen(PlayerContext playerContext, MainlineTeleportOpenRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);

        int teleportId = request.getTeleportId();
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), context.getSceneId());
        Asserts.isTrue(!mainlineScene.getTeleports().contains(teleportId), TipsCode.TELEPORT_OPENED);

        SceneTeleportCfg sceneTeleportCfg = sceneTeleportCache.getInMainlineIdTeleportIdIndex(context.getSceneId(), teleportId);
        List<CostThing> configCosts = sceneTeleportCfg.getOpenCosts();

        SceneOpening sceneOpening = mainlineScene.getOpeningTeleport(teleportId);
        boolean canOpen = mainlinePlayerService.checkSceneOpening(playerContext.getId(),
                sceneOpening, configCosts, GameCause.MAINLINE_TELEPORT_OPEN);
        if (canOpen) {
            mainlineScene.getOpeningTeleports().remove(teleportId);
            mainlineScene.getTeleports().add(teleportId);
            
            EventBus.post(BuildingTransmitEvent.of(playerContext.getId(), mainlineScene.getIdentity(), teleportId));
        }
        mainlineSceneService.update(mainlineScene);

        MainlineTeleportOpenResponse.Builder response = MainlineTeleportOpenResponse.newBuilder();
        if (canOpen) {
            response.setUnlockTeleport(teleportId);
        } else {
            response.setOpeningTeleport(PbHelper.build(sceneOpening));
        }
        return response.build();
    }

    @PlayerCmd
    public MainlineNpcOpenResponse npcOpen(PlayerContext playerContext, MainlineNpcOpenRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);

        int npcId = request.getNpcId();
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), context.getSceneId());
        Asserts.isTrue(!mainlineScene.getNpcs().contains(npcId), TipsCode.NPC_OPENED);

        SceneNpcCfg sceneNpcCfg = sceneNpcCache.getInSceneIdNpcIdIndex(context.getSceneId(), npcId);
        List<CostThing> configCosts = sceneNpcCfg.getQuestCosts();

        SceneOpening sceneOpening = mainlineScene.getOpeningNpc(npcId);
        boolean canOpen = mainlinePlayerService.checkSceneOpening(playerContext.getId(), sceneOpening, configCosts, GameCause.MAINLINE_NPC_OPEN);
        RewardReceipt rewardReceipt = null;
        if (canOpen) {
            mainlineScene.getOpeningNpcs().remove(npcId);
            mainlineScene.getNpcs().add(npcId);
            rewardReceipt = thingService.add(playerContext.getId(), sceneNpcCfg.getFinishRewards(), GameCause.MAINLINE_NPC_OPEN, NoticeType.TIPS);
            for (RewardDetail rewardDetail : rewardReceipt.getDetails()) {
                ItemCfg itemCfg = itemCache.getOrThrow(rewardDetail.getThingId());
                if (itemCfg.getKind() == ItemType.HERO.getType()) {
                    //尝试自动上阵
                    formationService.tryAutoIntoFormations(context, itemCfg.getId());
                }
            }

            EventBus.post(NpcInteractionEvent.of(playerContext.getId(), npcId));
        }
        mainlineSceneService.update(mainlineScene);


        MainlineNpcOpenResponse.Builder response = MainlineNpcOpenResponse.newBuilder();
        if (canOpen) {
            response.setUnlockNpc(npcId);
            response.setReceipt(PbHelper.build(rewardReceipt)); //canOpen=true时 rewardReceipt不可能为空
        } else {
            response.setOpeningNpc(PbHelper.build(sceneOpening));
        }
        return response.build();
    }

    @PlayerCmd
    public MainlineSetPortalResponse setPortal(PlayerContext playerContext, MainlineSetPortalRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), mainlinePlayer.getCurrScene());

        //TODO 验证坐标是否合法

        mainlineScene.setPortalX(request.getPortalX());
        mainlineScene.setPortalY(request.getPortalY());
        mainlineSceneService.update(mainlineScene);

        MainlineSetPortalResponse.Builder response = MainlineSetPortalResponse.newBuilder();
        response.setPortalX(mainlineScene.getPortalX());
        response.setPortalY(mainlineScene.getPortalY());
        return response.build();
    }

    @PlayerCmd
    public MainlineUsePortalResponse usePortal(PlayerContext playerContext, MainlineUsePortalRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), mainlinePlayer.getCurrScene());

        int portalX = mainlineScene.getPortalX();
        int portalY = mainlineScene.getPortalY();
        Asserts.isTrue(portalX != 0 && portalY != 0, TipsCode.MAINLINE_PORTAL_MISS);

        mainlineScene.setPortalX(0);
        mainlineScene.setPortalY(0);
        mainlineSceneService.update(mainlineScene);

        MainlineUsePortalResponse.Builder response = MainlineUsePortalResponse.newBuilder();
        response.setPortalX(portalX);
        response.setPortalY(portalY);
        return response.build();
    }

    @PlayerCmd
    public MainlineStartCampResponse startCamp(PlayerContext playerContext, MainlineStartCampRequest request) {
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
    	Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
    	
    	MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        Asserts.isTrue(!mainlinePlayer.isCamping(), TipsCode.CAMPING);
        
        mainlinePlayer.setCamping(true);
        mainlinePlayer.getCampProduces().clear();
        mainlinePlayer.setStartCampTime(DateTimeUtil.currMillis());
        mainlinePlayerService.update(mainlinePlayer);
        
        EventBus.post(CampSetupEvent.of(playerContext.getId()));

        MainlineStartCampResponse.Builder response = MainlineStartCampResponse.newBuilder();
        response.setCampAdvEndTime(mainlinePlayer.getCampAdvEndTime());
        return response.build();
    }

    @PlayerCmd
    public MainlineEndCampResponse endCamp(PlayerContext playerContext, MainlineEndCampRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        Asserts.isTrue(mainlinePlayer.isCamping(), TipsCode.NOT_CAMPING);

        long now = DateTimeUtil.currMillis();
        long newCampTime = now - mainlinePlayer.getStartCampTime();
        
        mainlinePlayer.setCamping(false);
        mainlinePlayer.setCampTime(mainlinePlayer.getCampTime() + newCampTime);
        mainlinePlayerService.update(mainlinePlayer);
        
        EventBus.post(CampTimeEvent.of(playerContext.getId(), newCampTime / DateTimeUtil.ONE_MINUTE_MILLIS));

        MainlineEndCampResponse.Builder response = MainlineEndCampResponse.newBuilder();
        response.setCurrCampTime(newCampTime);
        for (CampProduce campProduce : mainlinePlayer.getCampProduces().values()) {
            PbThing.Builder thing = PbThing.newBuilder();
            thing.setIdentity(campProduce.getItem());
            thing.setNum(campProduce.getNum());
            response.addThings(thing);
        }
        return response.build();
    }
    
    @PlayerCmd
    public MainlineOpenCampAdvResponse openCampAdv(PlayerContext playerContext, MainlineOpenCampAdvRequest request) {
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
    	
    	MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
    	Asserts.isTrue(mainlinePlayer.isCamping(), TipsCode.NOT_CAMPING);
    	Asserts.isTrue(mainlinePlayer.getCampAdvEndTime() < DateTimeUtil.currMillis(), TipsCode.CAMP_ADV_NOT_END);
    	Asserts.isTrue(mainlinePlayer.getCampAdvNum() < battleConstCache.getCamp_adv_max_num(), TipsCode.CAMP_ADV_NO_NUM);
    	
    	mainlinePlayer.setCampAdvNum(mainlinePlayer.getCampAdvNum() + 1);
    	mainlinePlayer.setCampAdvEndTime(DateTimeUtil.currMillis() + battleConstCache.getCamp_adv_time() * 1000L);
    	mainlinePlayerService.update(mainlinePlayer);
    	
    	EventBus.post(MainlineCampAdvEvent.of(playerContext.getId()));
    	
    	MainlineOpenCampAdvResponse.Builder response = MainlineOpenCampAdvResponse.newBuilder();
    	response.setCampAdvEndTime(mainlinePlayer.getCampAdvEndTime());
    	response.setCampAdvNum(mainlinePlayer.getCampAdvNum());
    	return response.build();
    }

    @PlayerCmd
    public MainlineHarvestResponse harvest(PlayerContext playerContext, MainlineHarvestRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.MAINLINE_CAN_HARVEST);

        HarvestThing harvestThing = context.getHarvest(request.getZoneId(), request.getHarvestId());
//        if (harvestThing == null) {
//            //FIXME 测试打印，需要删除
//            for (Zone zone : context.getZoneMap().values()) {
//                for (HarvestThing harvest : zone.getHarvests().values()) {
//                    if (harvest.getId() == request.getHarvestId()) {
//                        System.out.println("	区域：" + zone.getId() + " 收割物：" + harvest.getId() + " " + harvest.getIdentity());
//                    } else {
//                        System.out.println("区域：" + zone.getId() + " 收割物：" + harvest.getId() + " " + harvest.getIdentity());
//                    }
//                }
//            }
//        }
        Asserts.isTrue(harvestThing != null, TipsCode.MAINLINE_NO_HARVEST);
//        System.out.println("收割前：" + harvestThing.getId() + " 复活时间：" + harvestThing.getRebornTime() + " 当前时间：" + DateTimeUtil.currMillis());
        Asserts.isTrue(DateTimeUtil.currMillis() >= harvestThing.getRebornTime(), TipsCode.MAINLINE_HARVESTED);
        

        HarvestCfg harvestCfg = harvestCache.getOrThrow(harvestThing.getIdentity());

        harvestThing.setRebornTime(DateTimeUtil.currMillis() + harvestCfg.getRefreshTime() * DateTimeUtil.ONE_SECOND_MILLIS);
        RewardThing rewardThing = harvestCfg.getProduce();
        thingService.add(playerContext.getId(), rewardThing, GameCause.MAINLINE_HARVEST, NoticeType.SLIENT);

//        System.out.println("收割后：" + harvestThing.getId() + " 复活时间：" + harvestThing.getRebornTime());
        
        EventBus.post(HomeCollectionResourceEvent.of(playerContext.getId(), rewardThing.getThingId(), rewardThing.getNum()));

        MainlineHarvestResponse.Builder response = MainlineHarvestResponse.newBuilder();
        response.setZoneId(request.getZoneId());
        response.setHarvestId(harvestThing.getId());
        response.setRebornTime(harvestThing.getRebornTime());
        return response.build();
    }

    /**
     * 领取主线任务奖励
     *
     * @param playerContext
     * @param request
     * @return
     */
    @PlayerCmd
    public MainlineTaskRewardResponse taskReward(PlayerContext playerContext, MainlineTaskRewardRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());

        Task task = mainlinePlayer.getTask();
        Asserts.isTrue(task.getId() != 0, TipsCode.MAINLINE_TASK_ALLFIN);
        Asserts.isTrue(task.isDone(), TipsCode.MAINLINE_TASK_UNFIN);

        MainlineTaskCfg mainlineTaskCfg = mainlineTaskCache.getOrThrow(task.getId());

        MainlineTaskCfg nextMainlineTaskCfg = mainlineTaskCache.findInPreTaskIndex(task.getId());
        if (nextMainlineTaskCfg != null) {
        	Task nextTask = mainlinePlayer.getParallelTasks().remove(nextMainlineTaskCfg.getId());
        	Asserts.isTrue(nextTask != null, TipsCode.MAINLINE_TASK_ERROR, nextMainlineTaskCfg.getId());
        	//本来设计是根据任务是否并行来优化存储数据量，但是策划经常改表，只好把所有的主线任务都创建出来，用到的时候根据是否并行再决定是否沿用任务数据
        	if(nextMainlineTaskCfg.getParallel() == 1) {
        		mainlinePlayer.setTask(nextTask);
        	} else {
        		mainlinePlayer.setTask(Task.create(nextMainlineTaskCfg.getId()));
        	}
        	mainlineDotLogger.addTaskDot(playerContext.getId(), nextMainlineTaskCfg, MainlineTaskTiming.START);
            //检查一下任务完成
            mainlineTaskSystem.check(playerContext.getId(), mainlinePlayer.getTask());
        } else {
            //主线任务全部完成
        	mainlinePlayer.setTask(Task.create(0));
        }
        task.setStatus(TaskStatus.EMPTY.getValue());
        mainlinePlayer.getFinishTasks().add(task.getId());
        mainlinePlayerService.update(mainlinePlayer);

        thingService.add(playerContext.getId(), mainlineTaskCfg.getRewards(), GameCause.MAINLINE_FIN_TASK, NoticeType.TIPS);
        //任务完成打点
        mainlineDotLogger.addTaskDot(playerContext.getId(), mainlineTaskCfg, MainlineTaskTiming.FINISH);
        
        //检查新功能开启
        openFunctionService.checkAndPushNewFunctions(playerContext.getId());

        MainlineTaskRewardResponse.Builder response = MainlineTaskRewardResponse.newBuilder();
        response.setTask(PbHelper.build(mainlinePlayer.getTask()));
        return response.build();
    }

    @PlayerCmd
    public MainlineBoxInfoResponse boxInfo(PlayerContext playerContext, MainlineBoxInfoRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());

        MainlineBoxInfoResponse.Builder response = MainlineBoxInfoResponse.newBuilder();
        if (request.getBoxType() == 1) {
            response.setTreasureBox(build(mainlinePlayer.getBossTreasure()));
        } else if (request.getBoxType() == 2) {
            response.setTreasureBox(build(mainlinePlayer.getMapTreasure()));
        }
        response.setLeftBoxNum(battleConstCache.getMainline_box_max_num() - mainlinePlayer.getTreasureNum());
        return response.build();
    }

    public PbTreasureBox build(TreasureBox box) {
        PbTreasureBox.Builder builder = PbTreasureBox.newBuilder();
        builder.setBoxType(box.getBoxType());
        builder.setDisappearTime(box.getDisappearTime());
        builder.setBoxArg(box.getBoxArg());
        return builder.build();
    }

    @PlayerCmd
    public MainlineTakeBoxResponse takeBox(PlayerContext playerContext, MainlineTakeBoxRequest request) {
        MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());

        TreasureBox treasureBox = null;
        if (request.getBoxType() == MlTreasureBoxType.BOSS.getValue()) {
            treasureBox = mainlinePlayer.getBossTreasure();
        } else if (request.getBoxType() == MlTreasureBoxType.TIME.getValue()) {
            treasureBox = mainlinePlayer.getMapTreasure();
        }
        Asserts.isTrue(treasureBox != null && treasureBox.getBoxType() > 0, TipsCode.MAINLINE_NO_TYPE_BOX);
        Asserts.isTrue(treasureBox.getDisappearTime() > DateTimeUtil.currMillis(), TipsCode.MAINLINE_BOX_TIMEOUT);

        List<RewardThing> boxRewards = null;
        if(treasureBox.getBoxType() == MlTreasureBoxType.BOSS.getValue()) {
        	MainlineTreasureBoxCfg bossBoxCfg = mainlineTreasureBoxCache.getBossBox(treasureBox.getBoxArg());
        	Asserts.isTrue(bossBoxCfg != null, TipsCode.DESIGN_NOT_FOUND);
        	
        	boxRewards = itemRandomPackCache.randomReward(bossBoxCfg.getReward());
        } else {
        	MainlineTreasureBoxCfg timeBoxCfg = mainlineTreasureBoxCache.getTimeBox(treasureBox.getBoxArg());
        	
        	boxRewards = itemRandomPackCache.randomReward(timeBoxCfg.getReward());
        }
        //看广告双倍
        if(request.getWatchAd()) {
        	boxRewards = ThingUtil.multiplyReward(boxRewards, 2);
        }
        
        treasureBox.setBoxType(0);
        treasureBox.setDisappearTime(0);
        treasureBox.setBoxArg(0);

        mainlinePlayer.setTreasureNum(mainlinePlayer.getTreasureNum() + 1);
        mainlinePlayerService.update(mainlinePlayer);

        thingService.add(playerContext.getId(), boxRewards, GameCause.MAINLINE_BOX, NoticeType.TIPS, "WatchAd:" + request.getWatchAd());
        
        EventBus.post(MainlineOpenBoxEvent.of(playerContext.getId(), request.getWatchAd()));
        
        return MainlineTakeBoxResponse.getDefaultInstance();
    }

    @PlayerCmd
    public MainlineRefreshBoxResponse refreshBox(PlayerContext playerContext, MainlineRefreshBoxRequest request) {
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
    	
    	MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        LocalDateTime now = LocalDateTimeUtil.now();
        Asserts.isTrue(mainlinePlayer.getNextBoxTime().isBefore(now), TipsCode.MAINLINE_BOX_NOT_REFRESH);
        
        //是否还有宝箱数量
        if(mainlinePlayer.getTreasureNum() >= battleConstCache.getMainline_box_max_num()) {
        	MainlineRefreshBoxResponse.Builder response = MainlineRefreshBoxResponse.newBuilder();
            response.setNextBoxTime(LocalDateTimeUtil.toEpochMilli(mainlinePlayer.getDailyTime()));
            response.setHasBox(false);
            return response.build();
        }
        
        MainlineTreasureBoxCfg boxCfg = mainlineTreasureBoxCache.getTimeBox(context.getCurrZoneId());
        if(boxCfg == null) {
        	MainlineRefreshBoxResponse.Builder response = MainlineRefreshBoxResponse.newBuilder();
        	response.setNextBoxTime(LocalDateTimeUtil.toEpochMilli(mainlinePlayer.getNextBoxTime()));
            response.setHasBox(false);
            return response.build();
        }
        
        TreasureBox treasureBox = new TreasureBox();
        treasureBox.setBoxType(MlTreasureBoxType.TIME.getValue());
		treasureBox.setDisappearTime(
				DateTimeUtil.currMillis() + battleConstCache.getMainline_box_time() * DateTimeUtil.ONE_SECOND_MILLIS);
		treasureBox.setBoxArg(context.getCurrZoneId());
		
        mainlinePlayer.setMapTreasure(treasureBox);
        mainlinePlayer.setNextBoxTime(now.plusSeconds(battleConstCache.getMainline_box_time()));
        mainlinePlayerService.update(mainlinePlayer);

        MainlineRefreshBoxResponse.Builder response = MainlineRefreshBoxResponse.newBuilder();
        response.setNextBoxTime(LocalDateTimeUtil.toEpochMilli(mainlinePlayer.getNextBoxTime()));
        response.setHasBox(true);
        return response.build();
    }
    
    @PlayerCmd
    public MainlineChallengeBossResponse challengeBoss(PlayerContext playerContext, MainlineChallengeBossRequest request) {
    	MonsterConfig monsterConfig = monsterCache.getOrNull(request.getBossId());
    	if(monsterConfig != null && monsterConfig.getType() == 3) {
    		Player player = playerService.getEntity(playerContext.getId());
        	mainlineDotLogger.addBossDot(player.getId(), player.getFighting(), monsterConfig, MainlineBossTiming.CHALLENGE);
    	}
    	return MainlineChallengeBossResponse.getDefaultInstance();
    }

	@Override
	protected void dailyResetForOnlinePlayer(long playerId) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerId);
		if(!mainlinePlayer.isCamping()) {
			return;
		}
		//玩家在跨天的时候进行露营，那么处理应该是结算“昨天”的已露营时间，即从今天0点到开始露营的时间持续的分钟数
		//并将开始露营时间设置为当前0点时间
		long now = DateTimeUtil.currMillis();
		long newCampTime = now - mainlinePlayer.getStartCampTime();
        mainlinePlayer.setStartCampTime(now);
        mainlinePlayerService.update(mainlinePlayer);
        
        EventBus.post(CampTimeEvent.of(playerId, newCampTime / DateTimeUtil.ONE_MINUTE_MILLIS));
	}
	
	@PlayerCmd
	public MainlineKillMonsterNumResponse killNum(PlayerContext playerContext, MainlineKillMonsterNumRequest request) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
		
		MainlineKillMonsterNumResponse.Builder response = MainlineKillMonsterNumResponse.newBuilder();
		response.setKillNum(mainlinePlayer.getKillNum());
		return response.build();
	}
	
	@PlayerCmd
	public MainlineTakeHangRewardResponse takeHangReward(PlayerContext playerContext, MainlineTakeHangRewardRequest request) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
		if(!mainlinePlayer.isHangOpened()) {
			return MainlineTakeHangRewardResponse.getDefaultInstance();
		}
		if(mainlinePlayer.getHangBossId() <= 0) {
			return MainlineTakeHangRewardResponse.getDefaultInstance();
		}
		if(!mainlinePlayer.isHasHangReward()) {
			return MainlineTakeHangRewardResponse.getDefaultInstance();
		}
		
		long currTime = DateTimeUtil.currMillis();
		long hangTime = currTime - mainlinePlayer.getHangStartTime();
		long hangMinute = hangTime / DateTimeUtil.ONE_MINUTE_MILLIS;
		if(hangMinute <= 0) {
			return MainlineTakeHangRewardResponse.getDefaultInstance();
		}
		//TODO 看广告买特权可增加时长
		if(hangMinute > battleConstCache.getOffline_duration_time()) {
			hangMinute = battleConstCache.getOffline_duration_time();
		}
		List<OfflineBenefitsCfg> cfgList = offlineBenefitsCache.getInMonsterCollector(mainlinePlayer.getHangBossId());
		MonsterCfg monsterCfg = monsterCache.getOrThrow(mainlinePlayer.getHangBossId());
		
		Player player = playerService.getEntity(playerContext.getId());
		float factor = player.getLevel() / monsterCfg.getLevel();
		if(factor > 1) {
			factor = 1;
		}
		
		List<RewardThing> rewardList = new ArrayList<>();
		for(OfflineBenefitsCfg cfg : cfgList) {
			//奖励每份数量
			float eachNum = cfg.getMin() + (cfg.getMax() - cfg.getMin()) * factor;
			
			if(cfg.getItem() == ItemType.EXP.getThingId()) {
				//经验需要做衰减
				long countMinute = hangMinute;
				for(ExpLimitCfg expLimitCfg : expLimitCache.all()) {
					if(countMinute <= 0) {
						break;
					}
					int leftMonsterNum = 0;
					if(expLimitCfg.getLimitMax() <= 0) {
						leftMonsterNum = Integer.MAX_VALUE;
					} else {
						leftMonsterNum = expLimitCfg.getLimitMax() - mainlinePlayer.getKillNum();
					}
					if(leftMonsterNum <= 0) {
						continue;
					}
					int needMinute = leftMonsterNum / battleConstCache.getPer_minute_monster();
					long useMinute = 0;
					if(needMinute >= countMinute) {
						useMinute = countMinute;
					} else {
						useMinute = needMinute;
					}
					//奖励的份数
					float multiple = useMinute * 1.0f / cfg.getTime();
					//实际奖励数量
					long rewardNum = (long)(eachNum * multiple);
					
					rewardNum = (long)(rewardNum * expLimitCfg.getExpAdd());
					if(rewardNum <= 0) {
						continue;
					}
					
					rewardList.add(RewardThing.of(cfg.getItem(), rewardNum));
					//累加杀怪数
					mainlinePlayer.setKillNum(mainlinePlayer.getKillNum() + (int)(battleConstCache.getPer_minute_monster() * useMinute));
					
					countMinute -= useMinute;
				}
			} else {
				//奖励的份数
				float multiple = hangMinute * 1.0f / cfg.getTime();
				//实际奖励数量
				long rewardNum = (long)(eachNum * multiple);
				if(rewardNum <= 0) {
					continue;
				}
				rewardList.add(RewardThing.of(cfg.getItem(), rewardNum));
			}
		}
		
		mainlinePlayer.setHangStartTime(DateTimeUtil.currMillis());
		mainlinePlayer.setHasHangReward(false);
		mainlinePlayerService.update(mainlinePlayer);
		
		RewardReceipt rewardReceipt = thingService.add(playerContext.getId(), rewardList, GameCause.MAINLINE_HANG);
		
		MainlineTakeHangRewardResponse.Builder response = MainlineTakeHangRewardResponse.newBuilder();
		response.setHangTime(hangTime);
		response.setReceipt(PbHelper.build(rewardReceipt));
		return response.build();
	}
	
	@PlayerCmd
	public MainlineTimeBoxFarawayResponse timeBoxFaraway(PlayerContext playerContext, MainlineTimeBoxFarawayRequest request) {
		BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
    	
    	MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
        LocalDateTime now = LocalDateTimeUtil.now();
        Asserts.isTrue(mainlinePlayer.getNextBoxTime().isBefore(now), TipsCode.MAINLINE_BOX_NOT_REFRESH);
        
        TreasureBox treasureBox = mainlinePlayer.getMapTreasure();
        if(treasureBox != null && treasureBox.getBoxType() > 0) {
        	treasureBox.setBoxType(0);
    		treasureBox.setDisappearTime(0);
    		treasureBox.setBoxArg(0);
    		mainlinePlayer.setNextBoxTime(now.plusSeconds(10));//TODO 
    		mainlinePlayerService.update(mainlinePlayer);
        }
		
		MainlineTimeBoxFarawayResponse.Builder response = MainlineTimeBoxFarawayResponse.newBuilder();
		response.setNextBoxTime(LocalDateTimeUtil.toEpochMilli(mainlinePlayer.getNextBoxTime()));
		return response.build();
	}
	
//	static 前端刷新时间
//	
//	public static void main(String[] args) {
//		后端刷新时间
//		服务器当前时间
//		if(服务器当前时间>后端刷新时间) {
//			let 配置数据=从C场景宝箱配置表获取类型2，区域=当前区域的配置
//			if(配置数据 == null) {
//				前端刷新时间 = 服务器当前时间+10s
//			} else {
//				if(前端刷新时间==0) {
//					前端刷新时间 = 后端刷新时间
//				}
//				if(服务器当前时间 >前端刷新时间) {
//					发送刷新请求
//				}
//			}
//		}
//	}
}
