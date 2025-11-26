package com.xiugou.x1.game.server.module.dungeon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.BattleDrop;
import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.DungeonBookCache;
import com.xiugou.x1.design.module.DungeonCache;
import com.xiugou.x1.design.module.DungeonMonsterCache;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.SceneZoneCache;
import com.xiugou.x1.design.module.autogen.DungeonAbstractCache.DungeonCfg;
import com.xiugou.x1.design.module.autogen.DungeonMonsterAbstractCache.DungeonMonsterCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.dungeon.event.ChallengeDungeonsEvent;
import com.xiugou.x1.game.server.module.dungeon.event.DungeonLoseEvent;
import com.xiugou.x1.game.server.module.dungeon.model.Dungeon;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.mainline.service.MainlineSceneService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.privilegenormal.service.PrivilegeNormalService;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.dungeon.Dungeon.PbDungeonBattleParam;
import pb.xiugou.x1.protobuf.dungeon.Dungeon.PbDungeonBattleResult;

/**
 * @author YY
 */
@Component
public class DungeonBattleProcessor extends BaseBattleProcessor<PbDungeonBattleParam> {
    @Autowired
    private DungeonCache dungeonCache;
    @Autowired
    private DungeonMonsterCache dungeonMonsterCache;
    @Autowired
    private MainlineBattleProcessor mainlineBattleProcessor;
    @Autowired
    private ThingService thingService;
    @Autowired
    private MainlineSceneService mainlineSceneService;
    @Autowired
    private ItemRandomPackCache itemRandomPackCache;
    @Autowired
    private SceneZoneCache sceneZoneCache;
    @Autowired
    private DungeonBookCache dungeonBookCache;
    @Autowired
    private DungeonService dungeonService;
    @Autowired
    private OpenFunctionService openFunctionService;
    @Autowired
	private PrivilegeNormalService privilegeNormalService;
	@Autowired
	private BattleConstCache battleConstCache;

    @Override
    public IBattleType battleType() {
        return BattleType.DUNGEON;
    }

    @Override
    public void hasThisZone(BattleContext context, int zoneId) {
        DungeonCfg dungeonCfg = dungeonCache.getOrThrow(context.getSceneId());
        List<SceneZoneCfg> sceneZoneCfg = sceneZoneCache.getInSceneIdCollector(dungeonCfg.getSceneId());
        Asserts.isTrue(zoneId <= sceneZoneCfg.size(), TipsCode.DUNGEON_ZONE_OVER);
//        System.out.println("Dungeon 当前所在层数" + context.getCurrZoneId() + " 总层数" + sceneZoneCfg.size() + " 前端传参层数" + zoneId);
        Asserts.isTrue(zoneId == context.getCurrZoneId() + 1, TipsCode.DUNGEON_ZONE_WRONG);
        if(zoneId > 1) {
        	Zone preZone = context.getZoneMap().get(zoneId - 1);
        	boolean allDead = true;
        	for(Sprite sprite : preZone.getSprites()) {
        		if(sprite.isAlive()) {
        			allDead = false;
        			break;
        		}
        	}
        	Asserts.isTrue(allDead, TipsCode.DUNGEON_ZONE_NOT_CLEAR);
        }
    }

    @Override
    public Zone getZoneInfo(BattleContext context, int zoneId) {
        DungeonCfg dungeonCfg = dungeonCache.getOrThrow(context.getSceneId());

        SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(dungeonCfg.getSceneId(), zoneId);
        MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());
        Zone zone = context.buildZone(zoneId, mapData);
        //上一层的援军
        Sprite preAidSprite = null;
        if(zoneId > 1) {
        	Zone preZone = context.getZoneMap().get(zoneId - 1);
        	preAidSprite = preZone.getAidSprite();
        }
        //处理援军
        if(zoneId <= dungeonCfg.getAid().size()) {
        	int monsterId = dungeonCfg.getAid().get(zoneId - 1);
        	if(preAidSprite != null && preAidSprite.getConfigId() == monsterId) {
        		//沿用上一层的援军
        		zone.setAidSprite(preAidSprite);
        	} else {
        		Sprite aidHero = buildAidHero(context, zone, monsterId);
            	if(aidHero != null) {
            		zone.setAidSprite(aidHero);
            		//把援军添加到战斗上下文中，把移除的援军从战斗上下文中删除
            		context.addAidSprite(aidHero);
            		
            		if(preAidSprite != null) {
            			zone.setRemoveSpriteId(preAidSprite.getId());
                		context.removeAidSprite(preAidSprite.getId());
            		}
            	}
        	}
        } else {
        	if(preAidSprite != null) {
        		//沿用上一层的援军
        		zone.setAidSprite(preAidSprite);
        	}
        }
        return zone;
    }
    
    private Sprite buildAidHero(BattleContext context, Zone zone, int monsterId) {
    	IMonsterConfig monsterConfig = monsterCache.getConfig(monsterId);
        if (monsterConfig == null) {
        	logger.error("未找到怪物配置{}", monsterId);
            return null;
        }
        Hero ownHero = heroService.getEntity(context.getPlayerId(), monsterConfig.getIdentity());
    	if(ownHero != null) {
    		//已经拥有该英雄，则不进行增援
    		return null;
    	}
    	return context.spawnAidHero(monsterConfig, TeamSide.ATK);
    }

    @Override
    public List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds) {
        List<PbKillSprite> result = new ArrayList<>();
        DungeonCfg dungeonCfg = dungeonCache.getOrThrow(context.getSceneId());
        Dungeon dungeon = dungeonService.getEntity(context.getPlayerId());
        boolean firstEnter = !dungeon.getNormalDungeons().contains(dungeonCfg.getId());
        for (int spriteId : spriteIds) {
            Sprite sprite = context.getAllSprites().get(spriteId);
            if (sprite == null) {
                continue;
            }
            sprite.setAlive(false);

            PbKillSprite.Builder builder = PbKillSprite.newBuilder();
            builder.setId(sprite.getId());
            builder.setRebornTime(-1);
            if (sprite.getSide() == TeamSide.DEF) {
                List<RewardThing> rewards = null;
                MonsterConfig monsterCfg = monsterCache.getOrThrow(sprite.getIdentity());
                if (dungeonCfg.getType() == 2) {
                    //主线副本
                    rewards = getMainlineDungeonDropReward(monsterCfg);
                } else {
                    //地下城副本
                    rewards = getNormalDungeonDropReward(dungeonCfg, monsterCfg.getType(), firstEnter);
                }
                if (monsterCfg.getProduce().getThingId() > 0){
                    rewards.add(monsterCfg.getProduce());
                }
                if(dungeonCfg.getType() != 2 && privilegeNormalService.getEntity(context.getPlayerId()).isDungeonDropDouble()) {
                	rewards = ThingUtil.multiplyReward(rewards, battleConstCache.getPrivilege_normal_dungeon_drop());
                }

                for (RewardThing reward : rewards) {
                    builder.addDrops(PbHelper.build(reward));
                    context.addDrop(reward.getThingId(), reward.getNum());
                }
                //技能书
                builder.setBook(dungeonBookCache.dropSkillBook());
            }
            result.add(builder.build());
        }
        return result;
    }

    @Override
    public PbBattleEndData checkBattleEnd(BattleContext context) {
    	DungeonCfg dungeonCfg = dungeonCache.getOrThrow(context.getSceneId());
    	
        if (context.isAtkTeamAllDead()) {
            //战斗结束的时候移除上下文信息
            playerToContextMap.remove(context.getPlayerId());
            mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
            
            EventBus.post(DungeonLoseEvent.of(context.getPlayerId(), dungeonCfg.getType()));
            
            PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.FAIL);
//            System.out.println("PbBattleEndStatus.FAIL");
            return builder.build();
        }
        if(!context.isDefTeamAllDead()) {
        	PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
        	builder.setStatus(PbBattleEndStatus.BATTLING);
        	return builder.build();
        }
        
        List<SceneZoneCfg> sceneZoneCfg = sceneZoneCache.getInSceneIdCollector(dungeonCfg.getSceneId());
        if (context.getCurrZoneId() < sceneZoneCfg.size()) {
        	PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.DEF_ALL_DEAD_NEXT);
//            System.out.println("PbBattleEndStatus.DEF_ALL_DEAD_NEXT");
            return builder.build();
        }
        
		RewardReceipt rewardReceipt = null;
		if (dungeonCfg.getType() == 2) {
			// 主线副本
			rewardReceipt = settleMainlineDungeon(context, dungeonCfg);
		} else {
			// 地下城副本
			rewardReceipt = settleNormalDungeon(context, dungeonCfg);
		}

		openFunctionService.checkAndPushNewFunctions(context.getPlayerId());

		EventBus.post(ChallengeDungeonsEvent.of(context.getPlayerId(), dungeonCfg.getId()));

		// 战斗结束的时候移除上下文信息
		playerToContextMap.remove(context.getPlayerId());
		mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
		
		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		builder.setStatus(PbBattleEndStatus.WIN);
		PbDungeonBattleResult.Builder result = PbDungeonBattleResult.newBuilder();
		result.setDungeonId(dungeonCfg.getId());
		if (rewardReceipt != null) {
			for (RewardDetail reward : rewardReceipt.getDetails()) {
				result.addRewards(PbHelper.build(reward));
			}
		}
		builder.setData(result.build().toByteString());
        return builder.build();
    }

    //结算主线副本
    private RewardReceipt settleMainlineDungeon(BattleContext context, DungeonCfg dungeonCfg) {
        MainlineScene mainlineScene = mainlineSceneService.getOrThrow(context.getPlayerId(), dungeonCfg.getMainlineId());

        if (mainlineScene.getDungeons().contains(dungeonCfg.getId())) {
            return null;
        }

        mainlineScene.getDungeons().add(dungeonCfg.getId());
        mainlineSceneService.update(mainlineScene);

        Map<Integer, BattleDrop> drops = context.getDrops();
        List<RewardThing> rewardThings = new ArrayList<>();
        for (BattleDrop battleDrop : drops.values()) {
            rewardThings.add(RewardThing.of(battleDrop.getItem(), battleDrop.getNum()));
        }
        RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), rewardThings, GameCause.DUNGEON_PASS, NoticeType.SLIENT);
        return rewardReceipt;
    }


    //结算普通副本
    private RewardReceipt settleNormalDungeon(BattleContext context, DungeonCfg dungeonCfg) {
        if (!thingService.isEnough(context.getPlayerId(), dungeonCfg.getEnterCost())) {
            return null;
        }
        thingService.cost(context.getPlayerId(), dungeonCfg.getEnterCost(), GameCause.DUNGEON_PASS);

        Dungeon dungeon = dungeonService.getEntity(context.getPlayerId());
        if (!dungeon.getNormalDungeons().contains(dungeonCfg.getId())) {
            dungeon.getNormalDungeons().add(dungeonCfg.getId());
            dungeonService.update(dungeon);
        }

        Map<Integer, BattleDrop> drops = context.getDrops();
        List<RewardThing> rewardList = new ArrayList<>();
        for (BattleDrop battleDrop : drops.values()) {
            rewardList.add(RewardThing.of(battleDrop.getItem(), battleDrop.getNum()));
        }
        RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), rewardList, GameCause.DUNGEON_PASS);
        return rewardReceipt;
    }

    private List<RewardThing> getMainlineDungeonDropReward(MonsterConfig monsterCfg) {
        if (!monsterCfg.getRandomProduce().isEmpty()) {
            return itemRandomPackCache.randomReward(monsterCfg.getRandomProduce());
        }
        return new ArrayList<>();
    }

    /**
     * 
     * @param dungeonCfg
     * @param type 小怪类型
     * @param firstEnter 是否首次进入
     * @return
     */
    private List<RewardThing> getNormalDungeonDropReward(DungeonCfg dungeonCfg, int monsterType, boolean firstEnter) {
        List<Integer> drops = new ArrayList<>();
    	if(monsterType == 3) {
        	if(firstEnter) {
        		drops.addAll(dungeonCfg.getFirstBossDrop());
        	} else {
        		drops.addAll(dungeonCfg.getBossDrop());
        	}
        } else {
        	if(firstEnter) {
        		drops.addAll(dungeonCfg.getFirstDrop());
        	} else {
        		drops.addAll(dungeonCfg.getDrop());
        	}
        }
        return itemRandomPackCache.randomReward(drops);
    }
    @Override
    protected void canEnter(long playerId, int sceneId, PbDungeonBattleParam params) {
        DungeonCfg dungeonCfg = dungeonCache.getOrThrow(sceneId);
        CostThing enterCost = dungeonCfg.getEnterCost();
        if (enterCost != null && enterCost.getNum() != 0) {
            Asserts.isTrue(thingService.isEnough(playerId, enterCost), TipsCode.BAG_ITEM_LACK);
        }

        //前置条件
        if(dungeonCfg.getType() == 1) {
        	//普通副本
        	int preCopy = dungeonCfg.getPrecopy();
        	if(preCopy > 0) {
        		DungeonCfg dungeonConfig = dungeonCache.getOrThrow(preCopy);
                MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerId, dungeonConfig.getMainlineId());
                Asserts.isTrue(mainlineScene.getDungeons().contains(preCopy), TipsCode.DUNGEON_PRECONDITION_NOT_COMPLETED);
        	}
        } else if(dungeonCfg.getType() == 2) {
        	//主线场景副本
        	MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerId, dungeonCfg.getMainlineId());
            Set<Integer> dungeons = mainlineScene.getDungeons();
            Asserts.isTrue(!dungeons.contains(mainlineScene.getIdentity()), TipsCode.DUNGEON_ZONE_PASS);
        }
    }

    @Override
    protected void doEnterSetParam(BattleContext context, PbDungeonBattleParam params) {
    	
    }

    @Override
    public void onGiveUp(BattleContext context) {
        //战斗结束的时候移除上下文信息
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());

    }

    @Override
    public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
        DungeonMonsterCfg dungeonMonsterCfg = dungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex(context.getSceneId(), zoneId, monsterPoint.getId());

        List<Sprite> monsters = new ArrayList<>();
        for (int i = 0; i < dungeonMonsterCfg.getNum(); i++) {
            IMonsterConfig monsterConfig = monsterCache.getConfig(dungeonMonsterCfg.getMonsterId());
            if (monsterConfig == null) {
                logger.error("未找到{}怪物", monsterPoint.getId());
                continue;
            }
            Sprite sprite = context.spawnMonster(monsterConfig, TeamSide.DEF, dungeonMonsterCfg.getRefreshType(), (int) monsterPoint.getX(), (int) monsterPoint.getY());
            monsters.add(sprite);
        }
        return monsters;
    }

	@Override
	protected PbDungeonBattleParam parseParams(ByteString params) {
		try {
			return PbDungeonBattleParam.parseFrom(params);
		} catch (InvalidProtocolBufferException e) {
			return PbDungeonBattleParam.getDefaultInstance();
		}
	}
}
