/**
 *
 */
package com.xiugou.x1.game.server.module.tower.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.TowerCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache;
import com.xiugou.x1.design.module.autogen.TowerAbstractCache.TowerCfg;
import com.xiugou.x1.design.struct.Monster;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult.HeroAndPos;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.event.TowerChallengeTimesEvent;
import com.xiugou.x1.game.server.module.tower.event.TowerLoseEvent;
import com.xiugou.x1.game.server.module.tower.event.TowerWinEvent;
import com.xiugou.x1.game.server.module.tower.model.Tower;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.battle.Battle.PbSprite;
import pb.xiugou.x1.protobuf.tower.Tower.PbTowerBattleParam;
import pb.xiugou.x1.protobuf.tower.Tower.PbTowerBattleResult;

/**
 * @author YY
 */
public abstract class BaseTowerBattleProcessor extends BaseBattleProcessor<PbTowerBattleParam> {

    @Autowired
    protected MainlineBattleProcessor mainlineBattleProcessor;
    @Autowired
    protected TowerService towerService;
    @Autowired
    protected TowerCache towerCache;
    @Autowired
    protected HeroTypeCache heroTypeCache;
    @Autowired
    protected BattleConstCache battleConstCache;

    public abstract TowerType towerType();

    public abstract int getTowerLayer(Tower tower);

    public abstract boolean checkOptionDate();

    public BaseTowerBattleProcessor() {
    	super();
    	TowerService.register(this);
    }
    
    @Override
    public void hasThisZone(BattleContext context, int zoneId) {
        Asserts.isTrue(zoneId == 1, TipsCode.TOWER_ONLYONE_ZONE);
    }

    @Override
    public Zone getZoneInfo(BattleContext context, int zoneId) {
        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
        SceneZoneAbstractCache.SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(battleTypeCfg.getDefaultScene(), zoneId);
        MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());
        Zone zone = context.buildZone(zoneId, mapData);
        return zone;
    }

    public abstract int updateTowerLayer(Tower tower);

    @Override
    protected void canEnter(long playerId, int sceneId, PbTowerBattleParam params) {
        BattleContext context = getContext(playerId);
        Asserts.isTrue(context == null, TipsCode.TOWER_CHALLENGING);
        Asserts.isTrue(checkOptionDate(), TipsCode.TOWER_NO_CHALLENGE);
    }

    @Override
    protected void doEnterSetParam(BattleContext context, PbTowerBattleParam params) {
        Tower tower = towerService.getEntity(context.getPlayerId());
        context.setStage(this.getTowerLayer(tower));
        context.setStartTime(DateTimeUtil.currMillis());
        
        //打塔次数事件
        EventBus.post(TowerChallengeTimesEvent.of(context.getPlayerId(), towerType().getValue()));
    }

    @Override
    protected void checkFormation(long playerId, int mainHero, List<HeroAndPos> heroAndPoses) {
        super.checkFormation(playerId, mainHero, heroAndPoses);
        if (towerType().getAllowElement() == 0) {
            return;
        }
        for (HeroAndPos heroAndPos : heroAndPoses) {
            HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(heroAndPos.getHero().getIdentity());
            Asserts.isTrue(heroTypeCfg.getElement() == towerType().getAllowElement(), TipsCode.TOWER_NEED_ELEMENT);
        }
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
    public void onGiveUp(BattleContext context) {
        //战斗结束的时候移除上下文信息，并返回到主线场景中
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
    }

    @Override
    public PbBattleEndData checkBattleEnd(BattleContext context) {
        
        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(this.battleType().getValue());
        int battleTime = battleTypeCfg.getBattleTime();  //配置能用时间
        //TODO 时间判断的方案？
        long endTime = DateTimeUtil.currMillis() - context.getStartTime(); //实际使用时间
        if (context.isAtkTeamAllDead() || endTime > battleTime) {
            //战斗结束的时候移除上下文信息
            playerToContextMap.remove(context.getPlayerId());
            mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
            
            EventBus.post(TowerLoseEvent.of(context.getPlayerId()));
            
            PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.FAIL);
            return builder.build();
        }
        
        if(!context.isDefTeamAllDead()) {
        	PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.BATTLING);
            return builder.build();
        }
        
		Tower tower = towerService.getEntity(context.getPlayerId());
		int layer = updateTowerLayer(tower);
		towerService.update(tower);

		TowerCfg towerLayerCfg = towerCache.getInTypeLayerIndex(this.towerType().getValue(), layer);
		RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), towerLayerCfg.getRewards(),
				GameCause.TOWER_PASS, NoticeType.SLIENT);

		EventBus.post(TowerWinEvent.of(context.getPlayerId(), towerType(), layer));
		
		// 战斗结束的时候移除上下文信息
		playerToContextMap.remove(context.getPlayerId());
		mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
		
		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		builder.setStatus(PbBattleEndStatus.WIN);
		PbTowerBattleResult.Builder result = PbTowerBattleResult.newBuilder();
		result.setTowerType(towerType().getValue());
		result.setLayer(layer);
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
        Tower tower = towerService.getEntity(context.getPlayerId());
        TowerCfg towerCfg = towerCache.getInTypeLayerIndex(this.towerType().getValue() , this.getTowerLayer(tower) + 1);
        List<Monster> monster = towerCfg.getMonster();
        return getSpriteList(context, monster, monsterPoint, towerCfg.getRefreshType());
    }

    @Override
    public boolean canAutoIntoFormation(Formation formation, int heroIdentity) {
        if (!super.canAutoIntoFormation(formation, heroIdentity)) {
            return false;
        }
        if(towerType().getAllowElement() == 0) {
        	return true;
        }
        HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(heroIdentity);
        return heroTypeCfg.getElement() == towerType().getAllowElement();
    }

	@Override
	protected PbTowerBattleParam parseParams(ByteString params) {
		try {
			return PbTowerBattleParam.parseFrom(params);
		} catch (InvalidProtocolBufferException e) {
			return PbTowerBattleParam.getDefaultInstance();
		}
	}
}
