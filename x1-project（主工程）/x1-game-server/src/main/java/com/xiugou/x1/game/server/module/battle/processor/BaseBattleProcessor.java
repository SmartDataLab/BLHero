/**
 *
 */
package com.xiugou.x1.game.server.module.battle.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleProcessor;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.DungeonBookCache;
import com.xiugou.x1.design.module.HarvestCache;
import com.xiugou.x1.design.module.HeroLevelCache;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.SceneZoneCache;
import com.xiugou.x1.design.module.SkillBuffCache;
import com.xiugou.x1.design.module.SkillLevelCache;
import com.xiugou.x1.design.struct.Monster;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult.HeroAndPos;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.event.PlayerLogoutEvent;
import com.xiugou.x1.game.server.module.trainingCamp.service.TrainingCampService;

import pb.xiugou.x1.protobuf.battle.Battle.PbSprite;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormationPos;

/**
 * @author YY
 *
 */
public abstract class BaseBattleProcessor<P extends GeneratedMessageV3> implements IBattleProcessor {
	
    protected static Logger logger = LoggerFactory.getLogger(BaseBattleProcessor.class);

    @Autowired
    protected HeroService heroService;
    @Autowired
    protected MonsterCache monsterCache;
    @Autowired
    protected HarvestCache harvestCache;
    @Autowired
    protected BattleConstCache battleConstCache;
    @Autowired
    protected SkillLevelCache skillLevelCache;
    @Autowired
    protected HeroLevelCache heroLevelCache;
    @Autowired
    protected HeroTypeCache heroTypeCache;
    @Autowired
    protected PlayerContextManager playerContextManager;
    @Autowired
    protected ThingService thingService;
    @Autowired
    protected SceneZoneCache sceneZoneCache;
    @Autowired
    protected FormationService formationService;
    @Autowired
    protected SkillBuffCache skillBuffCache;
    @Autowired
    protected BattleTypeCache battleTypeCache;
    @Autowired
    protected TrainingCampService trainingCampService;
    @Autowired
    protected DungeonBookCache dungeonBookCache;

    //每个实现类都有自己的上下文缓存<玩家ID，战斗上下文>
    protected ConcurrentMap<Long, BattleContext> playerToContextMap = new ConcurrentHashMap<>();

    public BattleContext getContext(long playerId) {
        return playerToContextMap.get(playerId);
    }

    //玩家当前正在战斗的上下文，这是静态的
    private static ConcurrentMap<Long, BattleContext> currBattleContexts = new ConcurrentHashMap<>();

    public static BattleContext getCurrContext(long playerId) {
        return currBattleContexts.get(playerId);
    }

    public static void setCurr(BattleContext context) {
        currBattleContexts.put(context.getPlayerId(), context);
    }
    
    public static void cleanCurr(BattleContext context) {
    	currBattleContexts.remove(context.getPlayerId());
    }
    
    public static int getCurrBattleNum() {
    	return currBattleContexts.size();
    }

    public BaseBattleProcessor() {
        BattleManager.register(this);
    }

    public final BattleContext newContext(long playerId, int sceneId) {
        BattleContext context = new BattleContext(playerId, this.battleType(), sceneId, this, battleConstCache,
                monsterCache, heroLevelCache, heroTypeCache, null);
        return context;
    }

    public final BattleContext enter(long playerId, int sceneId, int mainHero, List<PbFormationPos> heroPosList, ByteString params) {
    	P pbParams = this.parseParams(params);
    	Asserts.isTrue(pbParams != null, TipsCode.BATTLE_PARAM_MISS);
    	
        //各系统玩家验证的是否可以进战斗
        canEnter(playerId, sceneId, pbParams);
        //更新布阵
        FormationResult formationResult = updateFormation(playerId, mainHero, heroPosList);

        BattleContext context = hasCurrContext(playerId, sceneId, pbParams);
        if (context == null) {
            context = this.newContext(playerId, sceneId);
        }

        context.setMainHeroIdentity(formationResult.getMainHero());
        List<Hero> heroes = formationResult.getList().stream().map(v -> v.getHero()).collect(Collectors.toList());
        context.setHeroes(heroes, TeamSide.ATK);
        
        //将客户端传过来的参数保存至战斗上下文中，允许在doEnterSetParam中对客户端参数进行修改，比如主线副本需要增加援军，则在参数中通知前端援军是个什么英雄
        context.setClientParams(params);
        doEnterSetParam(context, pbParams);
        
        playerToContextMap.put(playerId, context);
        BaseBattleProcessor.setCurr(context);
        return context;
    }
    
    @Override
	public boolean doEnterZone(BattleContext context, int zoneId) {
		return context.enterZone(zoneId);
	}

    //当前是否存在对应的战斗上下文
    public BattleContext hasCurrContext(long playerId, int sceneId, P params) {
        return null;
    }

    protected FormationResult updateFormation(long playerId, int mainHero, List<PbFormationPos> heroPosList) {
    	FormationResult formationResult = formationService.buildFormationResult(playerId, mainHero, heroPosList);

        //由各系统自己验证是否可以进战斗
        checkFormation(playerId, formationResult.getMainHero(), formationResult.getList());

        //保存编队并推送
        formationService.checkAndUpdate(playerId, battleType(), formationResult.getMainHero(), formationResult.getList());
        return formationResult;
    }

    protected void checkFormation(long playerId, int mainHero, List<HeroAndPos> heroAndPoses) {
    	System.out.println(battleType().getDesc() + " " + heroAndPoses.size() + " " + trainingCampService.getMaxTroopNum(playerId));
        Asserts.isTrue(heroAndPoses.size() <= trainingCampService.getMaxTroopNum(playerId), TipsCode.BATTLE_HERO_LIMIT);
    }

    protected abstract void canEnter(long playerId, int sceneId, P params);

    /**
     * 进入战斗时设置个性化参数
     * @param context
     * @param params
     */
    protected abstract void doEnterSetParam(BattleContext context, P params);
    
    protected abstract P parseParams(ByteString params);

    @Override
    public boolean canRebornWithTime() {
        return false;
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
    public void doTeamRevive(BattleContext context) {
        Asserts.isTrue(false, TipsCode.BATTLE_CANT_REVIVE_TEAM, this.battleType().getValue());
    }

    @Override
    public int switchHeroes(BattleContext context, int mainHero, List<PbFormationPos> heroPosList) {
        Asserts.isTrue(false, TipsCode.BATTLE_CANT_SWITCH_HERO, this.battleType().getValue());
        return 0;
    }

    /**
     * 是否能自动上阵到布阵中
     * @param formation
     * @param heroIdentity
     * @return
     */
    public boolean canAutoIntoFormation(Formation formation, int heroIdentity) {
        if (formation.getPositions().containsKey(heroIdentity)) {
            return false;
        }
        return formation.getPositions().size() < getFormationLimit(formation.getPid());
    }

    /**
     * 上阵英雄数量上限
     * @param playerId
     * @return
     */
    public int getFormationLimit(long playerId) {
        return trainingCampService.getMaxTroopNum(playerId);
    }

    /**
     * 刷怪
     */
    public List<Sprite> getSpriteList(BattleContext context, List<Monster> monsterList, MapMonsterPoint monsterPoint, int refreshType) {
        List<Sprite> monsters = new ArrayList<>();
        List<Monster> collect = monsterList.stream().filter(m -> m.getPos() == monsterPoint.getId()).collect(Collectors.toList());
        for (Monster monster : collect) {
            IMonsterConfig monsterConfig = monsterCache.getConfig(monster.getMonsterId());
            if (monsterConfig == null) {
                logger.error("未找到{}怪物", monsterPoint.getId());
                continue;
            }
            Sprite sprite = context.spawnMonster(monsterConfig, TeamSide.DEF, refreshType, (int) monsterPoint.getX(), (int) monsterPoint.getY());
            monsters.add(sprite);
        }
        return monsters;
    }
    
    @Subscribe
    private void listen(PlayerLogoutEvent event) {
    	if(this.battleType().isCleanOnLogout()) {
    		playerToContextMap.remove(event.getPid());
    	}
    	BattleContext battleContext = currBattleContexts.get(event.getPid());
    	if(battleContext != null && battleContext.getBattleType().isCleanOnLogout() && battleContext.getBattleType().getValue() == this.battleType().getValue()) {
    		currBattleContexts.remove(event.getPid());
    	}
    }
    
    public int getBattleNum() {
    	return playerToContextMap.size();
    }

	@Override
	public List<Sprite> specialRefreshMonster(BattleContext context, int zoneId) {
		return Collections.emptyList();
	}
}
