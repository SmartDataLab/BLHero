/**
 *
 */
package com.xiugou.x1.game.server.module.goldenpig.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.GoldenPigStageCache;
import com.xiugou.x1.design.module.SceneZoneCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.GoldenPigStageAbstractCache.GoldenPigStageCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.design.struct.Monster;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.goldenpig.event.ChallengeGoldenPigEvent;
import com.xiugou.x1.game.server.module.goldenpig.event.GoldenPigLoseEvent;
import com.xiugou.x1.game.server.module.goldenpig.model.GoldenPig;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.battle.Battle.PbSprite;
import pb.xiugou.x1.protobuf.goldenpig.GoldenPig.PbGoldenPigBattleParam;
import pb.xiugou.x1.protobuf.goldenpig.GoldenPig.PbGoldenPigBattleResult;

/**
 * @author YY
 */
@Component
public class GoldenPigBattleProcessor extends BaseBattleProcessor<PbGoldenPigBattleParam> {

    @Autowired
    private GoldenPigService goldenPigService;
    @Autowired
    private BattleTypeCache battleTypeCache;
    @Autowired
    private GoldenPigStageCache goldenPigStageCache;
    @Autowired
    private MainlineBattleProcessor mainlineBattleProcessor;
    @Autowired
    private SceneZoneCache sceneZoneCache;

    @Override
    public BattleType battleType() {
        return BattleType.GOLDEN_PIG;
    }

    @Override
    public void hasThisZone(BattleContext context, int zoneId) {
        Asserts.isTrue(zoneId == 1, TipsCode.GOLDENPIG_ONLYONE_ZONE);
    }

    @Override
    public Zone getZoneInfo(BattleContext context, int zoneId) {
        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
        SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(battleTypeCfg.getDefaultScene(), zoneId);
        MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());

        context.setStartTime(DateTimeUtil.currMillis());
        Zone zone = context.buildZone(zoneId, mapData);
        return zone;
    }

    @Override
    public void onGiveUp(BattleContext context) {
        //战斗结束的时候移除上下文信息
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
    }

    @Override
    public void doTeamRevive(BattleContext context) {
        //黄金组没有团队复活
        Asserts.isTrue(false, TipsCode.GOLDENPIG_NO_REVIVE);
    }

    @Override
    protected void canEnter(long playerId, int sceneId, PbGoldenPigBattleParam params) {
        GoldenPig goldenPig = goldenPigService.getEntity(playerId);

        Asserts.isTrue(thingService.isEnough(playerId, battleConstCache.getGold_pid_ticket()), TipsCode.GOLDENPIG_CHALLENGE_LIMIT);

        Asserts.isTrue(params.getStage() <= goldenPigStageCache.all().size(), TipsCode.GOLDENPIG_MAX_STAGE);
        Asserts.isTrue(params.getStage() <= goldenPig.getMaxStage() + 1, TipsCode.GOLDENPIG_CHALLENGE_FRONT);

        BattleContext context = getContext(playerId);
        Asserts.isTrue(context == null, TipsCode.GOLDENPIG_CHALLENGING);
    }

    @Override
    protected void doEnterSetParam(BattleContext context, PbGoldenPigBattleParam params) {
        context.setStage(params.getStage());
    }

    @Override
    public List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds) {
        List<PbKillSprite> result = new ArrayList<>();
        for (int spriteId : spriteIds) {
            Sprite sprite = context.getAllSprites().get(spriteId);
            if (sprite == null) {
                continue;
            }
            sprite.setAlive(false);

            PbKillSprite.Builder builder = PbKillSprite.newBuilder();
            builder.setId(sprite.getId());
            builder.setRebornTime(-1);
            result.add(builder.build());
        }
        return result;
    }

    @Override
    public PbBattleEndData checkBattleEnd(BattleContext context) {
        //todo 后期还需要处理一个暂停时间
        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
        long endTime = context.getStartTime() + battleTypeCfg.getBattleTime(); //配置结束时间
        
        if (context.isAtkTeamAllDead() || DateTimeUtil.currMillis() > endTime) {
            //战斗结束的时候移除上下文信息
            playerToContextMap.remove(context.getPlayerId());
            mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
            
            EventBus.post(GoldenPigLoseEvent.of(context.getPlayerId()));
            
            PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.FAIL);
            return builder.build();
        }
        boolean defAllDead = context.isDefTeamAllDead();
        if (!defAllDead) {
        	//怪物方没全死
        	PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
        	builder.setStatus(PbBattleEndStatus.BATTLING);
        	return builder.build();
        }

        //扣门票
        thingService.cost(context.getPlayerId(), battleConstCache.getGold_pid_ticket(), GameCause.GOLDENPIG_TICKET);

        GoldenPig goldenPig = goldenPigService.getEntity(context.getPlayerId());
        if (context.getStage() > goldenPig.getMaxStage()) {
            goldenPig.setMaxStage(context.getStage());
        }
        goldenPig.setChallengeNum(goldenPig.getChallengeNum() + 1);
        goldenPigService.update(goldenPig);

        GoldenPigStageCfg goldenPigStageCfg = goldenPigStageCache.getOrThrow(context.getStage());
        RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), goldenPigStageCfg.getReward(), GameCause.GOLDENPIG_PASS, NoticeType.SLIENT, "");

        EventBus.post(ChallengeGoldenPigEvent.of(context.getPlayerId()));
        //战斗结束的时候移除上下文信息
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
        
        PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
        builder.setStatus(PbBattleEndStatus.WIN);
        PbGoldenPigBattleResult.Builder result = PbGoldenPigBattleResult.newBuilder();
        result.setChallengeNum(goldenPig.getChallengeNum());
        result.setMaxStage(goldenPig.getMaxStage());
        for (RewardDetail reward : rewardReceipt.getDetails()) {
            result.addRewards(PbHelper.build(reward));
        }
        builder.setData(result.build().toByteString());
        return builder.build();
    }

    @Override
    public List<PbSprite> onRebornSprite(BattleContext context, List<Integer> spriteIds) {
        List<PbSprite> rebornSprites = new ArrayList<>();
        for (int spriteId : spriteIds) {
            Sprite sprite = context.getAllSprites().get(spriteId);
            if (sprite == null) {
                continue;
            }
            sprite.setAlive(true);
            sprite.setRebornTime(0);
            rebornSprites.add(sprite.build());
        }
        return rebornSprites;
    }

    @Override
    public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
        GoldenPigStageCfg goldenPigStageCfg = goldenPigStageCache.getOrThrow(context.getStage());
        Monster monsters = goldenPigStageCfg.getMonsters();
        return getSpriteList(context, Collections.singletonList(monsters), monsterPoint, goldenPigStageCfg.getRefreshType());
    }

	@Override
	protected PbGoldenPigBattleParam parseParams(ByteString params) {
		try {
            return PbGoldenPigBattleParam.parseFrom(params);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
	}
}
