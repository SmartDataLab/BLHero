/**
 *
 */
package com.xiugou.x1.game.server.module.mainline.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.ExpLimitCache;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.MainlineSceneCache;
import com.xiugou.x1.design.module.MainlineTreasureBoxCache;
import com.xiugou.x1.design.module.MainlineTreasureBoxCache.MlTreasureBoxType;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.autogen.MainlineSceneAbstractCache.MainlineSceneCfg;
import com.xiugou.x1.design.module.autogen.MainlineTreasureBoxAbstractCache.MainlineTreasureBoxCfg;
import com.xiugou.x1.design.module.autogen.MonsterAbstractCache.MonsterCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.event.KillMonsterEvent;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.mainline.event.MainlineAllDeadEvent;
import com.xiugou.x1.game.server.module.mainline.log.MainlineDotLogger;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.struct.TreasureBox;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormationPos;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineOpenNextMessage;
import pb.xiugou.x1.protobuf.mainline.Mainline.PbMainlineBattleParam;
import pb.xiugou.x1.protobuf.mainline.Mainline.PbMainlineBattleResult;
import pojo.xiugou.x1.pojo.log.mainline.MainlineBossTiming;

/**
 * @author YY
 */
@Component
public class MainlineBattleProcessor extends BaseBattleProcessor<PbMainlineBattleParam> {

	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	@Autowired
	private MainlineSceneService mainlineSceneService;
	@Autowired
	private ItemRandomPackCache itemRandomPackCache;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private BattleConstCache battleConstCache;
	@Autowired
	private MainlineDotLogger mainlineDotLogger;
	@Autowired
	private ExpLimitCache expLimitCache;
	@Autowired
    private MainlineTreasureBoxCache mainlineTreasureBoxCache;
	@Autowired
	private MainlineSceneCache mainlineSceneCache;
	
	@Override
	public BattleType battleType() {
		return BattleType.MAINLINE;
	}

	@Override
	public void canEnter(long playerId, int sceneId, PbMainlineBattleParam params) {
		MainlineScene mainlineScene = mainlineSceneService.getOrNull(playerId, sceneId);
		Asserts.isTrue(mainlineScene != null, TipsCode.MAINLINE_LOCK, sceneId);

		// TODO param.getStage()有没有超出最大难度
		Asserts.isTrue(mainlineScene.getMaxStage() + 1 >= params.getStage(), TipsCode.MAINLINE_STAGE_FRONT);
	}

	@Override
	public BattleContext hasCurrContext(long playerId, int sceneId, PbMainlineBattleParam params) {
		int stage = params.getStage();
		if (stage == 0) {
			stage = 1;
		}
		BattleContext context = getContext(playerId);
		if (context != null && context.getSceneId() == sceneId && context.getStage() == stage) {
			return context;
		}
		return null;
	}
	
	@Override
	protected void doEnterSetParam(BattleContext context, PbMainlineBattleParam params) {
		int stage = params.getStage();
		if (stage == 0) {
			stage = 1;
		}
		context.setStage(stage);

		MainlinePlayer player = mainlinePlayerService.getEntity(context.getPlayerId());
		player.setCurrBattleType(battleType().getValue());
		player.setCurrScene(context.getSceneId());
		mainlinePlayerService.update(player);
	}

	@Override
	public Zone getZoneInfo(BattleContext context, int zoneId) {
		SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(context.getSceneId(), zoneId);
		if (sceneZoneCfg.getSafeZone() == 1 || sceneZoneCfg.getSafeZone() == 0) {
			// TODO 进入安全区的时候复活所有参战英雄，并重置可复活次数
			MainlinePlayer entity = mainlinePlayerService.getEntity(context.getPlayerId());
			entity.setReviveNum(battleConstCache.getMainline_revive_num());
			mainlinePlayerService.update(entity);
			context.reviveAllHero();
		}
		MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());
		
		MainlineScene mainlineScene = mainlineSceneService.getOrThrow(context.getPlayerId(), context.getSceneId());
		Zone zone = context.buildZone(zoneId, mapData, false);
		List<Sprite> removeSprites = null;
		for(Sprite sprite : zone.getSprites()) {
			if(mainlineScene.getKillMonsters().contains(sprite.getConfigId())) {
				if(removeSprites == null) {
					removeSprites = new ArrayList<>();
				}
				removeSprites.add(sprite);
			}
		}
		if(removeSprites != null) {
			for(Sprite sprite : removeSprites) {
				zone.getSprites().remove(sprite);
				context.burySprite(sprite);
			}
		}
		return zone;
	}

	@Override
	public void onGiveUp(BattleContext context) {
		Asserts.isTrue(false, TipsCode.MAINLINE_NO_GIVEUP);
	}

	@Override
	public void hasThisZone(BattleContext context, int zoneId) {
		// TODO 读配置
	}

	@Override
	public int switchHeroes(BattleContext context, int mainHero, List<PbFormationPos> pbPosList) {
		// TODO 判断当前区域是不是非交战区域
		FormationResult formationResult = this.updateFormation(context.getPlayerId(), mainHero, pbPosList);
		List<Hero> heroes = formationResult.getList().stream().map(v -> v.getHero()).collect(Collectors.toList());
		context.setHeroes(heroes, TeamSide.ATK);
		return formationResult.getMainHero();
	}

	@Override
	public boolean canRebornWithTime() {
		return true;
	}

	@Override
	public void doTeamRevive(BattleContext context) {
		MainlinePlayer entity = mainlinePlayerService.getEntity(context.getPlayerId());
		// Asserts.isTrue(entity.getReviveNum() <= 0, TipsCode.BATTLE_NO_REVIVE);

		thingService.cost(context.getPlayerId(), battleConstCache.getMainline_revive_cost(), GameCause.MAINLINE_REVIVE);

		entity.setReviveNum(entity.getReviveNum() - 1);
		mainlinePlayerService.update(entity);
	}

	// 返回到主线场景中
	public void returnToMainlineScene(long playerId) {
		BattleContext context = this.getContext(playerId);
		if(context != null) {
			BaseBattleProcessor.setCurr(context);
		}
	}

	@Override
	public List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds) {
 		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(context.getPlayerId());
 		MainlineSceneCfg mainlineSceneCfg = mainlineSceneCache.getOrThrow(context.getSceneId());
 		
		long now = DateTimeUtil.currMillis();

		List<PbKillSprite> killSprites = new ArrayList<>();
		List<RewardThing> allRewardList = new ArrayList<>();

		boolean hasDropTreasureBox = false;
		Map<Integer, Integer> monsterMap = new HashMap<>();

		boolean needUpdate = false;
		for (int spriteId : spriteIds) {
			Sprite sprite = context.getAllSprites().get(spriteId);
			if (sprite == null) {
				continue;
			}
			if (sprite.getSide() == TeamSide.ATK) {
				sprite.setAlive(false);
				sprite.setRebornTime(
						context.getNow() + battleConstCache.getMainline_revive_time() * DateTimeUtil.ONE_SECOND_MILLIS);
				
				PbKillSprite.Builder builder = PbKillSprite.newBuilder();
				builder.setId(sprite.getId());
				builder.setRebornTime(sprite.getRebornTime());
				killSprites.add(builder.build());
				continue;
			}
			if (sprite.getRebornTime() > now || sprite.getRebornTime() < 0) {
				// 还没复活
				continue;
			}
			monsterMap.put(sprite.getIdentity(), monsterMap.getOrDefault(sprite.getIdentity(), 0) + 1); // 击杀怪物ID
			needUpdate = true;

			PbKillSprite pbSprite = killSprite(context, sprite, mainlinePlayer, allRewardList, hasDropTreasureBox);
			if(pbSprite.getHasBox()) {
				hasDropTreasureBox = true;
			}
			killSprites.add(pbSprite);
			
			if(sprite.getConfigId() == mainlineSceneCfg.getBossId() && mainlineSceneCfg.getNextScene() > 0) {
				MainlineScene mainlineScene = mainlineSceneService.getOrThrow(context.getPlayerId(), mainlineSceneCfg.getId());
				if(!mainlineScene.isOpenNext()) {
					mainlineScene.setOpenNext(true);
					mainlineSceneService.update(mainlineScene);
					//初始化下一场景的数据
					MainlineScene newScene = mainlineSceneService.newScene(context.getPlayerId(), mainlineSceneCfg.getNextScene());
					//推送开启下一场景入口
					MainlineOpenNextMessage.Builder message = MainlineOpenNextMessage.newBuilder();
					message.setCurrSceneId(mainlineScene.getIdentity());
					message.setCurrOpenNext(mainlineScene.isOpenNext());
					message.setNewScene(MainlineSceneService.build(newScene));
					playerContextManager.push(mainlineScene.getPid(), MainlineOpenNextMessage.Proto.ID, message.build());
				}
			}
		}

		if(!monsterMap.isEmpty()) {
			if (!allRewardList.isEmpty()) {
				if(mainlinePlayer.isCamping()) {
					//记录露营过程中产生的奖励
					mainlinePlayer.addCampProduces(allRewardList);
				}
				for(RewardThing rewardThing : allRewardList) {
					if(rewardThing.getNum() <= 0) {
						logger.error("奖励有0值{}", GsonUtil.toJson(allRewardList));
						break;
					}
				}
				thingService.add(context.getPlayerId(), allRewardList, GameCause.MAINLINE_DROP, NoticeType.SLIENT);
			}
			EventBus.post(KillMonsterEvent.of(mainlinePlayer.getPid(), monsterMap, allRewardList));
		}
		if(needUpdate) {
			mainlinePlayerService.update(mainlinePlayer);
		}
		return killSprites;
	}

	@Override
	public PbBattleEndData checkBattleEnd(BattleContext context) {
		if (!context.isAtkTeamAllDead()) {
			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.BATTLING);
			return builder.build();
		}
		MainlinePlayer entity = mainlinePlayerService.getEntity(context.getPlayerId());
		if (entity.isCamping()) {
			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.ATK_ALL_DEAD_CAMPING);
			return builder.build();
		}
		
		//TODO 攻方已经全死
		entity.setAllDeadNum(entity.getAllDeadNum() + 1);
		mainlinePlayerService.update(entity);
		
		EventBus.post(MainlineAllDeadEvent.of(context.getPlayerId()));
		
		if (entity.getReviveNum() <= 0) {
			PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
			builder.setStatus(PbBattleEndStatus.FAIL);
			return builder.build();
		}
		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		builder.setStatus(PbBattleEndStatus.WAIT_REVIVE);

		PbMainlineBattleResult.Builder result = PbMainlineBattleResult.newBuilder();
		result.setReviveNum(entity.getReviveNum());
		builder.setData(result.build().toByteString());
		return builder.build();
	}

	@Override
	public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
		// 主线战斗不用刷怪点的方式刷怪
		return Collections.emptyList();
	}

	@Override
	protected PbMainlineBattleParam parseParams(ByteString params) {
		try {
			return PbMainlineBattleParam.parseFrom(params);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
	}
	
	public void cleanBattle() {
		LocalDateTime now = LocalDateTime.now();
		for(BattleContext battleContext : this.playerToContextMap.values()) {
			if(playerContextManager.isOnline(battleContext.getPlayerId())) {
				continue;
			}
			Player player = playerService.getEntity(battleContext.getPlayerId());
			if(player.getLastLogoutTime().plusMinutes(30).isBefore(now)) {
				this.playerToContextMap.remove(battleContext.getPlayerId());
				
				BattleContext currContext = BaseBattleProcessor.getCurrContext(player.getId());
				if(currContext != null && currContext.getBattleType().getValue() == BattleType.MAINLINE.getValue()) {
					BaseBattleProcessor.cleanCurr(currContext);
				}
				logger.info("玩家{}-{}离线后回收战斗上下文", player.getId(), player.getNick());
			}
		}
	}
	
	private TreasureBox dropTreasureBox(Player player, MainlinePlayer mainlinePlayer, MonsterCfg monsterCfg) {
		if(mainlinePlayer.getTreasureNum() >= battleConstCache.getMainline_box_max_num()) {
			return null;
		}
		if(player.getLevel() - monsterCfg.getLevel() >= battleConstCache.getBoss_drop_level_limit()) {
			return null;
		}
		//击杀BOSS
		MainlineTreasureBoxCfg mainlineTreasureBoxCfg = mainlineTreasureBoxCache.getBossBox(monsterCfg.getId());
		if(mainlineTreasureBoxCfg == null) {
			return null;
		}
		TreasureBox treasureBox = new TreasureBox();
		treasureBox.setBoxType(MlTreasureBoxType.BOSS.getValue());
		treasureBox.setDisappearTime(DateTimeUtil.currMillis() + battleConstCache.getMainline_box_time() * DateTimeUtil.ONE_SECOND_MILLIS);
		treasureBox.setBoxArg(monsterCfg.getId());
		mainlinePlayer.setBossTreasure(treasureBox);
		return treasureBox;
	}
	
	private PbKillSprite killSprite(BattleContext context, Sprite sprite, MainlinePlayer mainlinePlayer, List<RewardThing> outRewards, boolean hasDropTreasureBox) {
		MonsterConfig monsterCfg = monsterCache.getOrThrow(sprite.getIdentity());
		sprite.setAlive(false);
		if(monsterCfg.getRefreshTime() > 0) {
			sprite.setRebornTime(context.getNow() + monsterCfg.getRefreshTime() * DateTimeUtil.ONE_SECOND_MILLIS);
		} else {
			sprite.setRebornTime(-1);
			MainlineScene mainlineScene = mainlineSceneService.getOrThrow(context.getPlayerId(), context.getSceneId());
			mainlineScene.getKillMonsters().add(monsterCfg.getId());
			mainlineSceneService.update(mainlineScene);
		}
		
		PbKillSprite.Builder builder = PbKillSprite.newBuilder();
		builder.setId(sprite.getId());
		if (monsterCfg.getType() == 3) {
			Player player = playerService.getEntity(context.getPlayerId());
			//判断BOSS掉落宝箱
			if(!hasDropTreasureBox) {
				TreasureBox treasureBox = dropTreasureBox(player, mainlinePlayer, monsterCfg);
				if(treasureBox != null) {
					builder.setHasBox(true);
				}
			}
			//挂机BOSS的ID
			if(mainlinePlayer.getHangBossId() == 0) {
				mainlinePlayer.setHangBossId(monsterCfg.getId());
			} else {
				MonsterConfig currBossCfg = monsterCache.getOrThrow(mainlinePlayer.getHangBossId());
				if(monsterCfg.getLevel() > currBossCfg.getLevel()) {
					mainlinePlayer.setHangBossId(monsterCfg.getId());
				}
			}
			mainlineDotLogger.addBossDot(player.getId(), player.getFighting(), monsterCfg, MainlineBossTiming.KILL);
		}
		mainlinePlayer.setKillNum(mainlinePlayer.getKillNum() + 1);
		//奖励相关
		List<RewardThing> rewardList = new ArrayList<>();
		float expRate = expLimitCache.getExpRate(mainlinePlayer.getKillNum());
		RewardThing rewardThing = ThingUtil.multiplyReward(monsterCfg.getProduce(), expRate);
		//高级露营中经验翻倍
        float campingAdvRate = 1.0f;
		if(mainlinePlayer.isCampingAdv()) {
			campingAdvRate = battleConstCache.getCamp_adv_exp_buff() / 10000.0f;
		}
		if(rewardThing.getNum() > 0) {
			if(campingAdvRate > 1) {
				rewardThing = ThingUtil.multiplyReward(rewardThing, campingAdvRate);
			}
			rewardList.add(rewardThing);
		}
		if (!monsterCfg.getRandomProduce().isEmpty()) {
			rewardList.addAll(itemRandomPackCache.randomReward(monsterCfg.getRandomProduce()));
		}
		for (RewardThing reward : rewardList) {
            builder.addDrops(PbHelper.build(reward));
		}
		builder.setRebornTime(sprite.getRebornTime());
		
		outRewards.addAll(rewardList);
		return builder.build();
	}
}
