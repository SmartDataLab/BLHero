/**
 *
 */
package com.xiugou.x1.game.server.module.villagedefense.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.util.DropUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.BattleTeam;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.DungeonBookCache;
import com.xiugou.x1.design.module.FairylandCache;
import com.xiugou.x1.design.module.FairylandMonsterCache;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.FairylandAbstractCache.FairylandCfg;
import com.xiugou.x1.design.module.autogen.FairylandMonsterAbstractCache.FairylandMonsterCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.Monster;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.recruit.struct.RandomData;
import com.xiugou.x1.game.server.module.villagedefense.event.VillageLevelEvent;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;
import com.xiugou.x1.game.server.module.villagedefense.model.VillageSystem;
import com.xiugou.x1.game.server.module.villagedefense.struct.VillageParam;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.village.Village.PbVillageBattleParam;
import pb.xiugou.x1.protobuf.village.Village.PbVillageBattleResult;

/**
 * @author YY
 */
@Component
public class VillageDefenseBattleProcessor extends BaseBattleProcessor<PbVillageBattleParam> {
    @Autowired
    private FairylandCache fairylandCache;
    @Autowired
    private FairylandMonsterCache fairylandMonsterCache;
    @Autowired
    private BattleTypeCache battleTypeCache;
    @Autowired
    private MainlineBattleProcessor mainlineBattleProcessor;
    @Autowired
    private ItemRandomPackCache itemRandomPackCache;
    @Autowired
    private VillageService villageService;
    @Autowired
    private DungeonBookCache dungeonBookCache;
    @Autowired
    private VillageSystemService villageSystemService;

    @Override
    public BattleType battleType() {
        return BattleType.VILLAGE_DEFENSE;
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
        Zone zone = context.buildZone(zoneId, mapData);
        return zone;
    }

    @Override
    public void onGiveUp(BattleContext context) {
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
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
            
            if (sprite.getSide() == TeamSide.DEF) {
                // 随机技能书
                builder.setBook(dungeonBookCache.dropSkillBook());

                // 掉落魂石
                int randomRate = RandomUtil.closeClose(1, 10000);
                if (randomRate <= battleConstCache.getVillage_soul_stone_rate()) {
                    VillageParam param = (VillageParam) context.getExtraParam();
                    //存储魂石经验
                    param.setExp(param.getExp() + 1);
                }
            }
            result.add(builder.build());
        }
        return result;
    }

    @Override
    public PbBattleEndData checkBattleEnd(BattleContext context) {
		BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
		RewardThing freeTicket = battleTypeCfg.getFreeTicket();

		if (context.isAtkTeamAllDead()) {
			// 扣除门票
			thingService.cost(context.getPlayerId(), CostThing.of(freeTicket.getThingId(), 1),
					GameCause.VILLAGE_CHALLENGE);
			// 战斗结束的时候移除上下文信息
			playerToContextMap.remove(context.getPlayerId());
			mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());

			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.FAIL);
			return builder.build();
		}

		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		PbVillageBattleResult.Builder result = PbVillageBattleResult.newBuilder();
		// 魂石足够时添加一个英雄
		VillageParam param = (VillageParam) context.getExtraParam();
		if (param.getExp() == 20) {
			List<Hero> heroes = randomHero(context.getPlayerId());
			for (Hero hero : heroes) {
				result.addHeros(PbHelper.build(hero));
			}
		}

		if (!context.isDefTeamAllDead()) {
			builder.setStatus(PbBattleEndStatus.BATTLING);
			return builder.build();
		}

		Village village = villageService.getEntity(context.getPlayerId());
		List<FairylandMonsterCfg> fairylandMonsterCfgs = fairylandMonsterCache
				.getInDifficultyCollector(village.getLevel() + (village.getRound() - 1) * 6); // 配置常量
		if (context.getStage() < fairylandMonsterCfgs.size()) {
			builder.setStatus(PbBattleEndStatus.DEF_ALL_DEAD_NEXT);
			return builder.build();
		}
		// 扣除门票
		thingService.cost(context.getPlayerId(), CostThing.of(freeTicket.getThingId(), 1), GameCause.VILLAGE_CHALLENGE);

		if (village.getLevel() < context.getSceneId()) {
			// 更新数据
			village.setLevel(context.getStage());
			villageService.update(village);
		}

		// 抛出通过事件
		EventBus.post(VillageLevelEvent.of(context.getPlayerId(), context.getSceneId()));

		// 合并奖励
		FairylandCfg fairylandCfg = fairylandCache.getOrThrow(context.getSceneId());
		List<RewardThing> rewardThings = new ArrayList<>();
		rewardThings.add(fairylandCfg.getReward());
		for (int randomId : fairylandCfg.getRandomReward()) {
			RewardThing rewardThing = itemRandomPackCache.randomReward(randomId);
			rewardThings.add(rewardThing);
		}
		RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), rewardThings, GameCause.VILLAGE_COMPLETE,
				NoticeType.SLIENT);
		// 战斗结束的时候移除上下文信息
		playerToContextMap.remove(context.getPlayerId());
		mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
		
		// 构建返回数据
		result.setLevel(context.getStage());
		for (RewardDetail detail : rewardReceipt.getDetails()) {
			result.addRewards(PbHelper.build(detail));
		}
		builder.setData(result.build().toByteString());
		builder.setStatus(PbBattleEndStatus.WIN);
		return builder.build();
    }

    @Override
    public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
        Village village = villageService.getEntity(context.getPlayerId());
        // context.getStage() 当作波数
        FairylandMonsterCfg fairylandMonsterCfg = fairylandMonsterCache
                .getInDifficultyWaveIndex(context.getSceneId() + (village.getRound() - 1) * 6, context.getStage());
        List<Monster> monsters = fairylandMonsterCfg.getMonsters();
        return getSpriteList(context, monsters, monsterPoint, fairylandMonsterCfg.getRefreshType());
    }

    @Override
    protected void canEnter(long playerId, int sceneId, PbVillageBattleParam params) {
        //判断当前时间是否属于结算时间
        VillageSystem villageSystem = villageSystemService.getEntity(playerId);
        int dungeonSettlementTime = battleConstCache.getDungeon_settlement_time();
        LocalDateTime nextResetTime = villageSystem.getNextResetTime();
        LocalDateTime endTime = LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.currMillis() + dungeonSettlementTime * DateTimeUtil.ONE_SECOND_MILLIS);
        Asserts.isTrue(nextResetTime.isAfter(endTime), TipsCode.DUNGEON_SETTLEMENT_TIME);

        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
        RewardThing freeTicket = battleTypeCfg.getFreeTicket();
        boolean enough = thingService.isEnough(playerId, CostThing.of(freeTicket.getThingId(), 1));
        Asserts.isTrue(enough, TipsCode.VILLAGE_TICKET_EMPTY);
    }

    @Override
    public int getFormationLimit(long playerId) {
        // TODO 村庄保卫战上阵英雄数量配常量
        return 4;
    }

    @Override
    protected void doEnterSetParam(BattleContext context, PbVillageBattleParam params) {
        context.setStage(params.getStage());
        context.setExtraParam(new VillageParam());
    }

    public List<Hero> randomHero(long pid) {
        BattleContext context = BaseBattleProcessor.getCurrContext(pid);
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        BattleTeam atkTeam = context.getAtkTeam();
        Map<Integer, Sprite> allSpriteMap = atkTeam.getAllSpriteMap();
        Map<Integer, Hero> heroMap = heroService.getHeroMap(pid);

        //筛选为出战英雄
        ArrayList<RandomData> randomData = new ArrayList<>();
        List<Integer> collect = heroMap.keySet().stream().filter(h -> !allSpriteMap.containsKey(h)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return null;
        }

        //随三个出来
        for (int identity : collect) {
            randomData.add(RandomData.of(identity, 1));
        }
        List<RandomData> randomDrop = DropUtil.randomDifDrop(randomData, 3);

        ArrayList<Hero> heroes = new ArrayList<>();
        for (RandomData random : randomDrop) {
            heroes.add(heroMap.get(random.getIdentity()));
        }
        return heroes;
    }

	@Override
	protected PbVillageBattleParam parseParams(ByteString params) {
		try {
            return PbVillageBattleParam.parseFrom(params);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
	}
}
