package com.xiugou.x1.game.server.module.evil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.ruler.util.DropUtil;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.EvilCatalogCache;
import com.xiugou.x1.design.module.EvilSoulCache;
import com.xiugou.x1.design.module.autogen.EvilCatalogAbstractCache.EvilCatalogCfg;
import com.xiugou.x1.design.module.autogen.EvilSoulAbstractCache.EvilSoulCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RandomItem;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.model.Bag;
import com.xiugou.x1.game.server.module.bag.service.BagService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import com.xiugou.x1.game.server.module.evil.model.Evil;
import com.xiugou.x1.game.server.module.evil.model.EvilFurnace;
import com.xiugou.x1.game.server.module.evil.service.EvilFurnaceService;
import com.xiugou.x1.game.server.module.evil.service.EvilService;
import com.xiugou.x1.game.server.module.evil.struct.EvilSpeedUpCost;
import com.xiugou.x1.game.server.module.evil.struct.RefineEvilData;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.refineevil.RefineEvil.PbRefineEvilDetail;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilInfoResponse;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilOnclickRequest;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilOnclickResponse;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilRewardRequest;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilRewardResponse;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilSoulRequest;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilSoulResponse;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilSpeedUpRequest;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilSpeedUpResponse;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilUpLevelRequest;
import pb.xiugou.x1.protobuf.refineevil.RefineEvil.RefineEvilUpLevelResponse;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote com.xiugou.x1.game.server.module.bag.BagGmHandler
 */
@Controller
public class RefineEvilHandler extends AbstractModuleHandler {
    @Autowired
    private EvilService evilService;
    @Autowired
    private EvilFurnaceService evilFurnaceService;
    @Autowired
    private ThingService thingService;
    @Autowired
    private EvilSoulCache evilSoulCache;
    @Autowired
    private BagService bagService;
    @Autowired
    private EvilCatalogCache evilCatalogCache;
    @Autowired
    private HeroService heroService;
    @Autowired
    private BattleConstCache battleConstCache;

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
        EvilFurnace entity = evilFurnaceService.getEntity(playerId);
        List<RefineEvilData> refineEvilDetail = entity.getRefineDetail();
        
        RefineEvilInfoResponse.Builder response = RefineEvilInfoResponse.newBuilder();
        for (int i = 0; i < refineEvilDetail.size(); i++) {
            PbRefineEvilDetail build = evilFurnaceService.build(refineEvilDetail.get(i), i);
            response.addRefineEvilDetail(build);
        }
        List<Evil> evilList = evilService.getEntities(playerId);
        for (Evil evil : evilList) {
            response.addEvils(evilService.build(evil));
        }
        response.setSpeedUpTime(entity.getSpeedUpTime());
        playerContextManager.push(playerId, RefineEvilInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public RefineEvilSoulResponse addSoul(PlayerContext playerContext, RefineEvilSoulRequest request) {
        int evilSoulId = request.getEvilSoulId();
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());

        List<RefineEvilData> refineList = evilFurnace.getRefineDetail();
        Asserts.isTrue(refineList.size() < battleConstCache.getEvil_refine_num(), TipsCode.EVI_FURNACE_ENOUGH);

        thingService.cost(playerContext.getId(), CostThing.of(evilSoulId, 1), GameCause.EVIL_SOUL_REFINE);

        RefineEvilData refineEvilData = new RefineEvilData();
        refineEvilData.setIdentity(evilSoulId);
        EvilSoulCfg evilSoulCfg = evilSoulCache.getOrThrow(evilSoulId);
        long nowTime = DateTimeUtil.currMillis();
        if (refineList.isEmpty()) {
            refineEvilData.setStartTime(nowTime);
        } else {
            //前一个位置的妖魂结束时间
            RefineEvilData preData = refineList.get(refineList.size() - 1);
            if (preData.getEndTime() > nowTime) {
                refineEvilData.setStartTime(preData.getEndTime());
            } else {
                refineEvilData.setStartTime(nowTime);
            }
        }
        refineEvilData.setEndTime(refineEvilData.getStartTime() + evilSoulCfg.getRefineTime() * DateTimeUtil.ONE_HOUR_MILLIS);
        refineList.add(refineEvilData);
        evilFurnaceService.update(evilFurnace);

        PbRefineEvilDetail pbRefineEvilDetail = evilFurnaceService.build(refineEvilData, refineList.indexOf(refineEvilData));
        RefineEvilSoulResponse.Builder response = RefineEvilSoulResponse.newBuilder();
        response.setRefineEvilDetail(pbRefineEvilDetail);
        return response.build();
    }


    @PlayerCmd
    public RefineEvilRewardResponse reward(PlayerContext playerContext, RefineEvilRewardRequest request) {
        int position = request.getPosition();
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());

        List<RefineEvilData> refineEvilDetail = evilFurnace.getRefineDetail();
        Asserts.isTrue(refineEvilDetail.size() > position, TipsCode.EVI_POS_ERROR);
        RefineEvilData refineEvilData = refineEvilDetail.get(position);
        Asserts.isTrue(refineEvilData.getEndTime() < DateTimeUtil.currMillis(), TipsCode.EVI_FURNACE_UNDONE);

        EvilSoulCfg evilSoulCfg = evilSoulCache.getOrThrow(refineEvilData.getIdentity());

        List<RewardThing> rewardThings = new ArrayList<>();
        rewardThings.addAll(evilSoulCfg.getReward());
        //发放奖励
        List<RandomItem> wightReward = evilSoulCfg.getWightReward();
        RandomItem weightItem = DropUtil.randomDrop(wightReward); //权重奖励
        rewardThings.add(RewardThing.of(weightItem.getItemId(), weightItem.getNum()));

        List<RandomItem> randomReward = evilSoulCfg.getRandomReward();
        RandomItem randomItem = DropUtil.randomIn10000Ratio(randomReward); //随机奖励
        if (randomItem != null) {
            rewardThings.add(RewardThing.of(randomItem.getItemId(), randomItem.getNum()));
        }

        refineEvilDetail.remove(position);
        evilFurnaceService.update(evilFurnace);

        RewardReceipt rewardReceipt = thingService.add(playerContext.getId(), rewardThings, GameCause.EVIL_SOUL_REFINE_REWARD, NoticeType.SLIENT);

        RefineEvilRewardResponse.Builder response = RefineEvilRewardResponse.newBuilder();
        for (int i = 0; i < refineEvilDetail.size(); i++) {
            PbRefineEvilDetail build = evilFurnaceService.build(refineEvilDetail.get(i), i);
            response.addRefineEvilDetail(build);
        }
        response.setReceipt(PbHelper.build(rewardReceipt));
        return response.build();
    }

    @PlayerCmd
    public RefineEvilOnclickResponse onclick(PlayerContext playerContext, RefineEvilOnclickRequest request) {
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());
        List<RefineEvilData> refineList = evilFurnace.getRefineDetail();
        Asserts.isTrue(refineList.size() < battleConstCache.getEvil_refine_num(), TipsCode.EVI_FURNACE_ENOUGH);

        Bag bag = bagService.getEntity(playerContext.getId());
        Map<Integer, ItemGrid> itemGridMap = bag.getIdGridMap();

        //计算妖魂道具
        List<EvilSoulCfg> evilSoulConfig = evilSoulCache.getSortList();
        List<CostThing> costThings = new ArrayList<>();
        List<Integer> evilSoulItem = new ArrayList<>();

        int leftRefineNum = battleConstCache.getEvil_refine_num() - refineList.size();
        for (EvilSoulCfg evilSoulCfg : evilSoulConfig) {
            ItemGrid itemGrid = itemGridMap.get(evilSoulCfg.getId());
            if (itemGrid == null) {
                continue;
            }
            for (int i = 0; i < itemGrid.getNum() && evilSoulItem.size() < leftRefineNum; i++) {
                evilSoulItem.add(itemGrid.getItemId());
            }
            if (evilSoulItem.size() >= leftRefineNum) {
                break;
            }
        }

        long nextStartTime = 0;
        if (refineList.isEmpty()) {
            nextStartTime = DateTimeUtil.currMillis();
        } else {
            RefineEvilData lastRefine = refineList.get(refineList.size() - 1);
            if (lastRefine.getEndTime() > DateTimeUtil.currMillis()) {
                nextStartTime = lastRefine.getEndTime();
            } else {
                nextStartTime = DateTimeUtil.currMillis();
            }
        }

        List<RefineEvilData> newRefineDatas = new ArrayList<>();
        //添加妖魂
        for (int itemId : evilSoulItem) {
            costThings.add(CostThing.of(itemId, 1));

            EvilSoulCfg evilSoulCfg = evilSoulCache.getOrThrow(itemId);
            RefineEvilData data = new RefineEvilData();
            data.setIdentity(itemId);
            data.setStartTime(nextStartTime);
            data.setEndTime(data.getStartTime() + evilSoulCfg.getRefineTime() * DateTimeUtil.ONE_HOUR_MILLIS);
            newRefineDatas.add(data);

            nextStartTime = data.getEndTime();
        }

        thingService.cost(playerContext.getId(), costThings, GameCause.EVIL_SOUL_REFINE);

        evilFurnace.getRefineDetail().addAll(newRefineDatas);
        evilFurnaceService.update(evilFurnace);

        RefineEvilOnclickResponse.Builder response = RefineEvilOnclickResponse.newBuilder();
        for (int i = 0; i < refineList.size(); i++) {
            PbRefineEvilDetail build = evilFurnaceService.build(refineList.get(i), i);
            response.addRefineEvilDetail(build);
        }
        return response.build();
    }

    @PlayerCmd
    public RefineEvilUpLevelResponse uplevel(PlayerContext playerContext, RefineEvilUpLevelRequest request) {
        Evil evil = evilService.getEntity(playerContext.getId(), request.getIdentity());
        Asserts.isTrue(evil != null, TipsCode.EVI_IDENTITY_WRONG);

        EvilCatalogCfg evilCatalogCfg = evilCatalogCache.findInIdentityLevelIndex(evil.getIdentity(), evil.getLevel());
        Asserts.isTrue(evilCatalogCfg.getCostBody() != 0, TipsCode.EVI_MAX_LEVEL);

        thingService.cost(playerContext.getId(), CostThing.of(evilCatalogCfg.getIdentity(), evilCatalogCfg.getCostBody()), GameCause.EVIL_LEVEL_UP);

        evil.setLevel(evil.getLevel() + 1);
        evilService.update(evil);

        heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.EVIL_LEVEL_UP); //计算属性

        RefineEvilUpLevelResponse.Builder response = RefineEvilUpLevelResponse.newBuilder();
        response.setEvil(evilService.build(evil));
        return response.build();
    }

    @PlayerCmd
    public RefineEvilSpeedUpResponse speedUp(PlayerContext playerContext, RefineEvilSpeedUpRequest request) {
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());
        List<RefineEvilData> refineList = evilFurnace.getRefineDetail();
        Asserts.isTrue(!refineList.isEmpty(), TipsCode.EVI_FURNACE_EMPTY);

        long nowTime = DateTimeUtil.currMillis();

        //加速卡效果 加速当前的操作
        if (request.getType() == 0) {
            int runningIndex = -1;
            for (int i = 0; i < refineList.size(); i++) {
                RefineEvilData data = refineList.get(i);
                if (data.getEndTime() <= nowTime) {
                    continue;
                }
                if (data.getStartTime() <= nowTime && nowTime <= data.getEndTime()) {
                    runningIndex = i;
                    break;
                }
            }
            Asserts.isTrue(runningIndex >= 0, TipsCode.EVI_NO_RUNNING);

            RefineEvilData currEvil = refineList.get(runningIndex);
            long costMinute = currEvil.calculateLeftMinute();

            //计算加速当前炼妖需要的消耗
            EvilSpeedUpCost costResult = calculateSpeedCost(evilFurnace.getSpeedUpTime(), costMinute, request.getBuyTime());
            Asserts.isTrue(!costResult.getCostList().isEmpty(),TipsCode.EVI_SPEEDUP_TIME_LACK);
            thingService.cost(playerContext.getId(), costResult.getCostList(), GameCause.EVIL_BUY_SPEEDUP_TIME);

            //修改当前炼妖的结束时间，并对其后续的炼妖时间进行修正
            currEvil.setEndTime(currEvil.getEndTime() - costResult.getCostMinute() * DateTimeUtil.ONE_MINUTE_MILLIS);
            if (currEvil.getEndTime() < nowTime) {
                currEvil.setEndTime(nowTime);
            }
            for (int i = runningIndex + 1; i < refineList.size(); i++) {
                RefineEvilData nextData = refineList.get(i);
                RefineEvilData preNextData = refineList.get(i - 1);

                EvilSoulCfg evilSoulCfg = evilSoulCache.getOrThrow(nextData.getIdentity());
                nextData.setStartTime(preNextData.getEndTime());
                nextData.setEndTime(nextData.getStartTime() + evilSoulCfg.getRefineTime() * DateTimeUtil.ONE_HOUR_MILLIS);
            }
        } else {
            //加速全部
            //计算所有炼妖总共还需要的分钟数
            long needMinute = 0;
            for (int i = 0; i < refineList.size(); i++) {
                RefineEvilData data = refineList.get(i);
                needMinute += data.calculateLeftMinute();
            }
            Asserts.isTrue(needMinute > 0, TipsCode.EVI_FURNACE_EMPTY);

            EvilSpeedUpCost costResult = calculateSpeedCost(evilFurnace.getSpeedUpTime(), needMinute, request.getBuyTime());
            Asserts.isTrue(!costResult.getCostList().isEmpty(),TipsCode.EVI_SPEEDUP_TIME_LACK);
            thingService.cost(playerContext.getId(), costResult.getCostList(), GameCause.EVIL_BUY_SPEEDUP_TIME);

            long leftMinute = costResult.getCostMinute();
            for (int i = 0; i < refineList.size() && leftMinute > 0; i++) {
                RefineEvilData data = refineList.get(i);
                long currNeedMinute = data.calculateLeftMinute();
                if (currNeedMinute <= 0) {
                    continue;
                }

                if (leftMinute >= currNeedMinute) {
                    data.setEndTime(nowTime);
                } else {
                    if (i > 0) {
                        RefineEvilData preData = refineList.get(i - 1);
                        long endTime = data.getEndTime() - data.getStartTime() - leftMinute * DateTimeUtil.ONE_MINUTE_MILLIS;

                        data.setStartTime(preData.getEndTime());
                        data.setEndTime(data.getStartTime() + endTime);
                    } else {
                        data.setEndTime(data.getEndTime() - leftMinute * DateTimeUtil.ONE_MINUTE_MILLIS);
                    }
                }
                leftMinute -= currNeedMinute;
            }
        }
        evilFurnaceService.update(evilFurnace);

        RefineEvilSpeedUpResponse.Builder response = RefineEvilSpeedUpResponse.newBuilder();
        for (int i = 0; i < refineList.size(); i++) {
            PbRefineEvilDetail build = evilFurnaceService.build(refineList.get(i), i);
            response.addRefineEvilDetail(build);
        }
        return response.build();
    }

    private EvilSpeedUpCost calculateSpeedCost(long hasSpeedMinute, long needCostMinute, boolean buyTime) {
        EvilSpeedUpCost result = new EvilSpeedUpCost();
        if (hasSpeedMinute >= needCostMinute) {
            result.getCostList().add(CostThing.of(ItemType.EVIL_SPEED_UP, needCostMinute));
        } else {
            if (buyTime) {
                if (hasSpeedMinute > 0) {
                    result.getCostList().add(CostThing.of(ItemType.EVIL_SPEED_UP, hasSpeedMinute));
                }
                CostThing evilSpeedCost = battleConstCache.getEvil_speed_cost();
                int num = (int) Math.ceil((needCostMinute - hasSpeedMinute) / 60.0f);
                result.getCostList().add(ThingUtil.multiplyCost(evilSpeedCost, num));
            } else {
                if (hasSpeedMinute > 0) {
                    needCostMinute = hasSpeedMinute;
                    result.setCostMinute(hasSpeedMinute);
                    result.getCostList().add(CostThing.of(ItemType.EVIL_SPEED_UP.getThingId(), needCostMinute));
                }
            }
        }
        result.setCostMinute(needCostMinute);
        return result;
    }
}
