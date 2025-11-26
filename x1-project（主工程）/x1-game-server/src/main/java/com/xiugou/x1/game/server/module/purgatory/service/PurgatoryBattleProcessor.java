package com.xiugou.x1.game.server.module.purgatory.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.BattleDrop;
import com.xiugou.x1.battle.BattleTeam;
import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.PurgatoryCache;
import com.xiugou.x1.design.module.PurgatoryMonsterCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.PurgatoryAbstractCache.PurgatoryCfg;
import com.xiugou.x1.design.module.autogen.PurgatoryMonsterAbstractCache.PurgatoryMonsterCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.purgatory.event.PurgatoryLevelEvent;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.purgatory.model.PurgatorySystem;
import com.xiugou.x1.game.server.module.purgatory.struct.PurgatoryParam;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PbPurgatoryBattleParam;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PbPurgatoryBattleResult;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote 炼狱轮回
 */
@Component
public class PurgatoryBattleProcessor extends BaseBattleProcessor<PbPurgatoryBattleParam> {
	@Autowired
	private PurgatoryCache purgatoryCache;
	@Autowired
	private MainlineBattleProcessor mainlineBattleProcessor;
	@Autowired
	private ItemRandomPackCache itemRandomPackCache;
	@Autowired
	private PurgatoryService purgatoryService;
	@Autowired
	private PurgatoryMonsterCache purgatoryMonsterCache;
	@Autowired
	private PurgatorySystemService purgatorySystemService;

	@Override
	public IBattleType battleType() {
		return BattleType.PURGATORY;
	}

	@Override
	public void hasThisZone(BattleContext context, int zoneId) {
		PurgatoryCfg purgatoryCfg = purgatoryCache.getOrThrow(context.getSceneId());
		List<SceneZoneCfg> sceneZoneCfg = sceneZoneCache.getInSceneIdCollector(purgatoryCfg.getSceneId());
		Asserts.isTrue(zoneId <= sceneZoneCfg.size(), TipsCode.PURGATORY_ZONE_OVER);
		Asserts.isTrue(zoneId == context.getCurrZoneId() + 1, TipsCode.PURGATORY_ZONE_WRONG);
	}

	@Override
	public Zone getZoneInfo(BattleContext context, int zoneId) {
		PurgatoryCfg purgatoryCfg = purgatoryCache.getOrThrow(context.getSceneId());
		SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(purgatoryCfg.getSceneId(), zoneId);
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
		PurgatoryParam purgatoryParam = (PurgatoryParam) context.getExtraParam();

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
				// 加成奖励走另一套
				if (purgatoryParam.getPlusFlag() == 1) {
					PurgatoryCfg purgatoryCfg = purgatoryCache.getOrThrow(context.getSceneId());

					List<Integer> dropList = new ArrayList<>();
					dropList.addAll(purgatoryCfg.getBoostDrop());
					dropList.addAll(purgatoryCfg.getBossDrop());
					for (int drop : dropList) {
						RewardThing reward = itemRandomPackCache.randomReward(drop);
						builder.addDrops(PbHelper.build(reward));
						context.addDrop(reward.getThingId(), reward.getNum());
					}
				} else {
					MonsterConfig monsterCfg = monsterCache.getOrThrow(sprite.getIdentity());
					// 随机掉落
					if (!monsterCfg.getRandomProduce().isEmpty()) {
						List<RewardThing> rewardThings = itemRandomPackCache
								.randomReward(monsterCfg.getRandomProduce());
						for (RewardThing reward : rewardThings) {
							builder.addDrops(PbHelper.build(reward));
							context.addDrop(reward.getThingId(), reward.getNum());
						}
					}
					// 固定掉落
					RewardThing expProduce = monsterCfg.getProduce();
					if(expProduce.getThingId() > 0) {
						builder.addDrops(PbHelper.build(expProduce));
						context.addDrop(expProduce.getThingId(), expProduce.getNum());
					}
				}
			}
			result.add(builder.build());
		}
		return result;
	}

	@Override
	public PbBattleEndData checkBattleEnd(BattleContext context) {
		boolean atkAllDead = context.isAtkTeamAllDead();

		Purgatory purgatory = purgatoryService.getEntity(context.getPlayerId());
		PurgatoryMonsterCfg purgatoryMonsterCfg = purgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex(
				context.getSceneId() + (purgatory.getRound() - 1) * 2, context.getCurrZoneId(), 1);
		long useTime = DateTimeUtil.currMillis() - context.getStartTime(); // 实际使用时间
		long endTime = context.getStartTime() + purgatoryMonsterCfg.getTime() * DateTimeUtil.ONE_SECOND_MILLIS; // 配置结束时间

		BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(battleType().getValue());
		RewardThing freeTicket = battleTypeCfg.getFreeTicket();

		if (atkAllDead || useTime > endTime) {
			thingService.cost(context.getPlayerId(), CostThing.of(freeTicket.getThingId(), 1),
					GameCause.PURGATORY_CHALLENGE);
			// 战斗结束的时候移除上下文信息
			playerToContextMap.remove(context.getPlayerId());
			mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());

			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.FAIL);
			return builder.build();
		}
		if (!context.isDefTeamAllDead()) {
			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.BATTLING);
			return builder.build();
		}
		PurgatoryCfg purgatoryCfg = purgatoryCache.getOrThrow(context.getSceneId());
		List<SceneZoneCfg> sceneZoneCfg = sceneZoneCache.getInSceneIdCollector(purgatoryCfg.getSceneId());

		if (context.getCurrZoneId() < sceneZoneCfg.size()) {
			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.DEF_ALL_DEAD_NEXT);
			return builder.build();
		}
		thingService.cost(context.getPlayerId(), CostThing.of(freeTicket.getThingId(), 1),
				GameCause.PURGATORY_CHALLENGE);
		// 扣除加成次数
		PurgatoryParam extraParam = (PurgatoryParam) context.getExtraParam();
		if (extraParam.getPlusFlag() == 1) {
			if (purgatory.getFreeTimes() < 2) {
				purgatory.setFreeTimes(purgatory.getFreeTimes() + 1);
			} else {
				purgatory.setPlusTimes(purgatory.getPlusTimes() - 1);
			}
		}
		Attr attr = purgatory.getAttrs().get(context.getSceneId());
		if (attr == null) {
			attr = purgatoryService.getAttrByStashId(purgatoryCfg.getAttrStash());
			purgatory.getAttrs().put(context.getSceneId(), attr);
		}
		// 排行榜事件 副本通关事件
		if (context.getSceneId() > purgatory.getLevel()) {
			purgatory.setLevel(context.getSceneId());
		}
		// 重置刷新次数
		purgatory.setRefineTimes(0);
		purgatoryService.update(purgatory);

		// 发放奖励
		List<RewardThing> rewardThings = new ArrayList<>();
		Map<Integer, BattleDrop> drops = context.getDrops();
		for (BattleDrop battleDrop : drops.values()) {
			rewardThings.add(RewardThing.of(battleDrop.getItem(), battleDrop.getNum()));
		}
		RewardReceipt rewardReceipt = thingService.add(context.getPlayerId(), rewardThings, GameCause.PURGATORY_PASS,
				NoticeType.SLIENT);

		// TODO 推荐阵容加成 后续再做
		// 新通过则随机属性 否则拿老属性

		EventBus.post(PurgatoryLevelEvent.of(context.getPlayerId(), purgatory.getLevel()));

		// 战斗结束的时候移除上下文信息
		playerToContextMap.remove(context.getPlayerId());
		mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
		
		PbPurgatoryBattleResult.Builder result = PbPurgatoryBattleResult.newBuilder();
		result.setLevel(context.getSceneId());
		result.setMaxLevel(purgatory.getLevel());
		for (RewardDetail reward : rewardReceipt.getDetails()) {
			result.addRewards(PbHelper.build(reward));
		}
		result.setAttr(PbHelper.build(attr));
		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		builder.setStatus(PbBattleEndStatus.WIN);
		builder.setData(result.build().toByteString());
		return builder.build();
	}

	@Override
	public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
		Purgatory purgatory = purgatoryService.getEntity(context.getPlayerId());
		PurgatoryMonsterCfg purgatoryMonsterCfg = purgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex(
				context.getSceneId() + (purgatory.getRound() - 1) * 2, zoneId, monsterPoint.getId());
		List<Keyv> monster = purgatoryMonsterCfg.getMonster();
		List<Sprite> monsters = new ArrayList<>();
		for (Keyv keyv : monster) {
			IMonsterConfig monsterConfig = monsterCache.getConfig(keyv.getKey());
			if (monsterConfig == null) {
				logger.error("未找到{}怪物", monsterPoint.getId());
				continue;
			}
			for (int i = 0; i < keyv.getValue(); i++) {
				Sprite sprite = context.spawnMonster(monsterConfig, TeamSide.DEF, purgatoryMonsterCfg.getRefreshType(),
						(int) monsterPoint.getX(), (int) monsterPoint.getY());
				monsters.add(sprite);
			}
		}
		return monsters;
	}

	@Override
	protected void canEnter(long playerId, int sceneId, PbPurgatoryBattleParam params) {
		// 判断当前时间是否属于结算时间
		PurgatorySystem purgatorySystem = purgatorySystemService.getEntity(playerId);
		int dungeonSettlementTime = battleConstCache.getDungeon_settlement_time();
		LocalDateTime nextResetTime = purgatorySystem.getNextResetTime();
		LocalDateTime endTime = LocalDateTimeUtil
				.ofEpochMilli(DateTimeUtil.currMillis() + dungeonSettlementTime * DateTimeUtil.ONE_SECOND_MILLIS);
		Asserts.isTrue(nextResetTime.isAfter(endTime), TipsCode.DUNGEON_SETTLEMENT_TIME);

		Purgatory purgatory = purgatoryService.getEntity(playerId);
		Asserts.isTrue(sceneId <= purgatory.getLevel() + 1, TipsCode.PURGATORY_BEFORE_LEVEL_UNDONE);

		if (params.getPlusFlag() == 1) {
			Asserts.isTrue(purgatory.getFreeTimes() < 2 || purgatory.getPlusTimes() > 0,
					TipsCode.PURGATORY_PLUS_TIMES_EMPTY);
		}
	}

	@Override
	protected void doEnterSetParam(BattleContext context, PbPurgatoryBattleParam params) {
		Purgatory purgatory = purgatoryService.getEntity(context.getPlayerId());

		PurgatoryParam purgatoryParam = new PurgatoryParam();
		purgatoryParam.setPlusFlag(params.getPlusFlag());
		// TODO 计算布阵加成

		purgatoryParam.setFormationPlus(0);
		purgatoryParam.setRound(purgatory.getRound());

		context.setExtraParam(purgatoryParam); // 玩家是否选择加成奖励 1为加成 0为不加成

		Map<Integer, Attr> attrs = purgatory.getAttrs();// 额外加成
		BattleAttr battleAttr = new BattleAttr();
		for (Attr attr : attrs.values()) {
			battleAttr.addById(attr.getAttrId(), attr.getValue());
		}
		BattleTeam atkTeam = context.getAtkTeam();
		List<Sprite> aliveSpriteList = atkTeam.getAliveSpriteList();
		for (Sprite sprite : aliveSpriteList) {
			sprite.getBattleAttr().merge(battleAttr);
		}
	}

	@Override
	protected PbPurgatoryBattleParam parseParams(ByteString params) {
		try {
			return PbPurgatoryBattleParam.parseFrom(params);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
	}
}
