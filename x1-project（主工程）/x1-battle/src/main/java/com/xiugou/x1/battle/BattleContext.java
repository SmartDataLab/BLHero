/**
 * 
 */
package com.xiugou.x1.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.gaming.tool.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IBattleConstCache;
import com.xiugou.x1.battle.config.IBuffConfigCache;
import com.xiugou.x1.battle.config.IHeroLevelConfigCache;
import com.xiugou.x1.battle.config.IHeroTypeConfig;
import com.xiugou.x1.battle.config.IHeroTypeConfigCache;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.config.IMonsterConfigCache;
import com.xiugou.x1.battle.constant.BattleResult;
import com.xiugou.x1.battle.constant.SpriteType;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.mapdata2.MonsterPoint;
import com.xiugou.x1.battle.mapdata2.ResourcePoint;
import com.xiugou.x1.battle.result.IActionEvent;
import com.xiugou.x1.battle.sprite.HarvestThing;
import com.xiugou.x1.battle.sprite.IBattleSprite;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.battle.template.TemplateSprite;

/**
 * @author YY
 *
 */
public class BattleContext extends SeedContext {

	private static Logger logger = LoggerFactory.getLogger(BattleContext.class);

	private final AtomicInteger idGen = new AtomicInteger(0);
	private final AtomicInteger buffIdGen = new AtomicInteger(0);
	private long playerId;

	// 进入过的区域
	private final Map<Integer, Zone> zoneMap = new HashMap<>();
	// 所有精灵的集合，atk跟def双方都在
	private final Map<Integer, Sprite> allSprites = new HashMap<>();

	private final IMonsterConfigCache monsterConfigCache;
	private final IBattleConstCache battleConstCache;
	private final IHeroTypeConfigCache heroTypeCache;
	private final IBuffConfigCache buffConfigCache;

	private final IBattleProcessor battleProcessor;

	private final IBattleType battleType;
	// 场景ID，不同战斗场景ID的语意不同，主线战斗是主线ID，地下城战斗是地下城ID
	private final int sceneId;
	// 当前所在的区域ID，主线时其语意是区域ID，地下城时其语意是层数
	private int currZoneId;
	// 难度
	private int stage;
	// 扩展参数
	private Object extraParam;

	// 主英雄标识
	private int mainHeroIdentity;

	// 事件返回列表
	private final List<IActionEvent> actionEvents = new ArrayList<>();
	// 某一场战斗或某一次技能后的掉落
	private final Map<Integer, BattleDrop> drops = new HashMap<>();
	// 杀怪数量，用于处理任务计数
	private final Map<Integer, Integer> kills = new HashMap<>();

	private final BattleTeam atkTeam = new BattleTeam(this, TeamSide.ATK);
	private final BattleTeam defTeam = new BattleTeam(this, TeamSide.DEF);
	// 当前时间
	// private long now;

	// 是否进行日志打印
	private boolean log = true;
	// 是否已经战斗结束
	private BattleResult battleResult = BattleResult.NONE;
	// 战斗开始时间
	private long startTime;

	private ByteString clientParams;

	public BattleContext(long playerId, IBattleType battleType, int sceneId, IBattleProcessor battleProcessor,
			IBattleConstCache battleConstCache, IMonsterConfigCache monsterConfigCache,
			IHeroLevelConfigCache heroLevelCache, IHeroTypeConfigCache heroTypeCache,
			IBuffConfigCache buffConfigCache) {
		this.playerId = playerId;
		this.battleType = battleType;
		this.sceneId = sceneId;
		this.monsterConfigCache = monsterConfigCache;
		this.battleProcessor = battleProcessor;
		this.battleConstCache = battleConstCache;
		this.heroTypeCache = heroTypeCache;
		this.buffConfigCache = buffConfigCache;
	}

	public Sprite spawnHero(IBattleSprite battleSprite, TeamSide side, int groupId) {
		IHeroTypeConfig typeConfig = heroTypeCache.getConfig(battleSprite.getIdentity());

		Sprite sprite = new Sprite(idGen.incrementAndGet(), SpriteType.HERO);
		sprite.setIdentity(battleSprite.getIdentity());
		sprite.setLevel(battleSprite.getLevel());
		sprite.setSide(side);
		sprite.setSpriteEntity(battleSprite);
		sprite.setElement(typeConfig.getElement());
		sprite.setHp(battleSprite.getPanelAttr().getMaxHp());
		sprite.getBattleAttr().merge(battleSprite.getPanelAttr());
		return sprite;
	}

	public Sprite spawnAidHero(IBattleSprite battleSprite, TeamSide side) {
		// 援军是通过怪物配置创建的英雄
		Sprite sprite = new Sprite(idGen.incrementAndGet(), SpriteType.HERO);
		sprite.setIdentity(battleSprite.getIdentity());
		sprite.setLevel(battleSprite.getLevel());
		// 援军需要记录配置ID，用于判断援军是否需要替换
		sprite.setConfigId(battleSprite.getConfigId());
		sprite.setSide(side);
		sprite.setSpriteEntity(battleSprite);
		sprite.setHp(battleSprite.getPanelAttr().getMaxHp());
		sprite.getBattleAttr().merge(battleSprite.getPanelAttr());
		return sprite;
	}

	public void reviveAllHero() {
		while (this.atkTeam.getDeadSpriteList().size() > 0) {
			Sprite sprite = this.atkTeam.getDeadSpriteList().get(0);
			sprite.setAlive(true);
			sprite.setRebornTime(0);
			sprite.setHp(sprite.getSpriteEntity().getPanelAttr().getMaxHp());
			this.atkTeam.reliveSprite(sprite);
		}
		this.battleResult = BattleResult.NONE;
	}

	public Sprite spawnMonster(IBattleSprite battleSprite, TeamSide side, int ctype, int cx, int cy) {
		Sprite sprite = new Sprite(idGen.incrementAndGet(), SpriteType.MONSTER);
		sprite.setIdentity(battleSprite.getConfigId());
		sprite.setLevel(battleSprite.getLevel());
		sprite.setConfigId(battleSprite.getConfigId());
		sprite.setSide(side);
		sprite.setElement(0);
		sprite.setSpriteEntity(battleSprite);
		sprite.setHp(battleSprite.getPanelAttr().getMaxHp());
		sprite.setCtype((byte) ctype);
		sprite.setCx(cx);
		sprite.setCy(cy);
		sprite.getBattleAttr().merge(battleSprite.getPanelAttr());
		for (Attr attr : battleConstCache.getMonsterInitAttr()) {
			sprite.getBattleAttr().addById(attr.getAttrId(), attr.getValue());
		}
		return sprite;
	}
	
	public Sprite spawnFromTemplate(TemplateSprite templateSprite, TeamSide side, SpriteType spriteType) {
		Sprite sprite = new Sprite(idGen.incrementAndGet(), spriteType);
		sprite.setIdentity(templateSprite.getIdentity());
		sprite.setLevel(templateSprite.getLevel());
		sprite.setConfigId(templateSprite.getConfigId());
		sprite.setSide(side);
		sprite.setElement(0);
		sprite.setSpriteEntity(templateSprite);
		sprite.setHp(templateSprite.getPanelAttr().getMaxHp());
		sprite.getBattleAttr().merge(templateSprite.getPanelAttr());
		return sprite;
	}
	

	public void setHeroes(List<? extends IBattleSprite> heroes, TeamSide side) {
		if (side == TeamSide.ATK) {
			setHeroes(heroes, atkTeam);
		} else {
			setHeroes(heroes, defTeam);
		}
	}

	private void setHeroes(List<? extends IBattleSprite> heroes, BattleTeam battleTeam) {
		// 收集当前战斗中已经出战的英雄<精灵ID，精灵>
		Map<Integer, Sprite> currHeroes = new HashMap<>();
		for (Sprite sprite : battleTeam.getAllSpriteMap().values()) {
			if (sprite.getSpriteType() == SpriteType.HERO) {
				currHeroes.put(sprite.getIdentity(), sprite);
			}
		}
		// 本次战斗需要切换成的英雄
		List<Sprite> battleHeroes = new ArrayList<>();
		for (IBattleSprite iHero : heroes) {
			// 相同的英雄在场景里面是不会换对象的，因为要保存血量，TODO 是否应该将延续英雄的处理放在具体的玩法系统上
			Sprite sprite = currHeroes.get(iHero.getIdentity());
			if (sprite != null) {
				battleHeroes.add(sprite);
				continue;
			}
			sprite = spawnHero(iHero, battleTeam.getSide(), 0);
			battleHeroes.add(sprite);
		}
		// 移除队伍中原来的单位，切换场景换人
		for (Sprite sprite : battleTeam.getAllSpriteMap().values()) {
			allSprites.remove(sprite.getId());
		}
		// 添加新的参战英雄
		battleTeam.cleanSprites();
		for (Sprite sprite : battleHeroes) {
			allSprites.put(sprite.getId(), sprite);
			battleTeam.addSprite(sprite);
		}
	}

	public Sprite addAidHero(IBattleSprite iHero, TeamSide side) {
		Sprite sprite = spawnHero(iHero, side, 0);
		addAidSprite(sprite);
		return sprite;
	}

	public void addAidSprite(Sprite sprite) {
		allSprites.put(sprite.getId(), sprite);
		if (sprite.getSide() == TeamSide.ATK) {
			atkTeam.addSprite(sprite);
		} else {
			defTeam.addSprite(sprite);
		}
	}

	public void removeAidSprite(int aidSpriteId) {
		Sprite sprite = allSprites.get(aidSpriteId);
		if (sprite.getSide() == TeamSide.ATK) {
			atkTeam.burySprite(sprite);
		} else {
			defTeam.addSprite(sprite);
		}
	}

	public Zone buildZone(int zoneId, MapData2 mapData) {
		return buildZone(zoneId, mapData, true);
	}

	public Zone buildZone(int zoneId, MapData2 mapData, boolean spawnMonster) {
		Zone zone = zoneMap.get(zoneId);
//		if (zone != null) {
//			return zone;
//		}
		zone = new Zone();
		zone.setId(zoneId);

		if (mapData != null) {
			// 初始化怪物分组
			for (MonsterPoint monsterPoint : mapData.getMonster_point()) {
				for (int i = 0; i < monsterPoint.getMax_num(); i++) {
					IMonsterConfig monsterConfig = monsterConfigCache.getConfig(monsterPoint.getId());
					if (monsterConfig == null) {
						logger.error("{}战斗，区域{}，未找到{}怪物", battleType.getDesc(), zoneId, monsterPoint.getId());
						continue;
					}
					Sprite sprite = this.spawnMonster(monsterConfig, TeamSide.DEF, 1, (int) monsterPoint.getX(),
							(int) monsterPoint.getY());
					sprite.setFogArea(monsterPoint.getFog_area());
					zone.getSprites().add(sprite);

					allSprites.put(sprite.getId(), sprite);
					defTeam.addSprite(sprite);
				}
			}

			// 初始化刷怪点
			for (MapMonsterPoint monsterPoint : mapData.getMap_monster_point()) {
				List<Sprite> monsters = battleProcessor.refreshMonster(this, zoneId, monsterPoint);
				for (Sprite sprite : monsters) {
					zone.getSprites().add(sprite);

					allSprites.put(sprite.getId(), sprite);
					defTeam.addSprite(sprite);
				}
			}

			// 初始化树木、矿石等收割分组
			for (ResourcePoint harvestPoint : mapData.getResource_point()) {
				for (int i = 0; i < harvestPoint.getMax_num(); i++) {
					HarvestThing harvest = new HarvestThing();
					harvest.setId(idGen.incrementAndGet());
					harvest.setIdentity(harvestPoint.getId());
					harvest.setCtype((byte) 1);
					harvest.setCx((int) harvestPoint.getX());
					harvest.setCy((int) harvestPoint.getY());
					harvest.setFogArea(harvestPoint.getFog_area());
					zone.getHarvests().put(harvest.getId(), harvest);
				}
			}
		}
		List<Sprite> specialMonsters = battleProcessor.specialRefreshMonster(this, zoneId);
		for (Sprite sprite : specialMonsters) {
			zone.getSprites().add(sprite);

			allSprites.put(sprite.getId(), sprite);
			defTeam.addSprite(sprite);
		}
		
		zoneMap.put(zone.getId(), zone);
		return zone;
	}
	
	public void addMonsterFromGm(int zoneId, Sprite sprite) {
		Zone zone = zoneMap.get(zoneId);
		if (zone == null) {
			return;
		}
		zone.getSprites().add(sprite);
		allSprites.put(sprite.getId(), sprite);
		defTeam.addSprite(sprite);
	}

	public boolean enterZone(int zoneId) {
		Zone zone = zoneMap.get(zoneId);
		if (zone == null) {
			return false;
		}
		this.currZoneId = zoneId;
		return true;
	}

	public void removeZone(int zoneId) {
		Zone zone = zoneMap.remove(zoneId);
		if (zone == null) {
			return;
		}
		for (Sprite sprite : zone.getSprites()) {
			this.allSprites.remove(sprite.getId());
		}
	}

	public int getCurrZoneId() {
		return currZoneId;
	}

	public IBattleProcessor getBattleProcessor() {
		return battleProcessor;
	}

	public void doOutOfBattle(List<Integer> spriteIds) {
		for (Integer spriteId : spriteIds) {
			Sprite sprite = this.allSprites.get(spriteId);
			if (sprite == null) {
				continue;
			}
			if (sprite.isDeath()) {
				continue;
			}
			sprite.setInBattle(false);
		}
	}

	public IBattleConstCache getBattleConstCache() {
		return battleConstCache;
	}

	public Map<Integer, Sprite> getAllSprites() {
		return allSprites;
	}

	public void addEvent(IActionEvent actionEvent) {
		this.actionEvents.add(actionEvent);
	}

	public long getPlayerId() {
		return playerId;
	}

	/**
	 * 场景ID，不同战斗场景ID的语意不同，主线战斗是主线ID，地下城战斗是地下城ID
	 * 
	 * @return
	 */
	public int getSceneId() {
		return sceneId;
	}

	public List<Sprite> getFriends(Sprite sprite) {
		if (sprite.getSide() == TeamSide.ATK) {
			return atkTeam.getAliveSpriteList();
		} else {
			return defTeam.getAliveSpriteList();
		}
	}

	public Sprite getFriends(Sprite sprite, Integer friendId) {
		if (sprite.getSide() == TeamSide.ATK) {
			return atkTeam.getSprite(friendId);
		} else {
			return defTeam.getSprite(friendId);
		}
	}

	public List<Sprite> getEnemys(Sprite sprite) {
		if (sprite.getSide() == TeamSide.ATK) {
			return defTeam.getAliveSpriteList();
		} else {
			return atkTeam.getAliveSpriteList();
		}
	}

	public Sprite getEnemy(Sprite sprite, Integer enemyId) {
		if (sprite.getSide() == TeamSide.ATK) {
			return defTeam.getSprite(enemyId);
		} else {
			return atkTeam.getSprite(enemyId);
		}
	}

	public void addDrop(int item, long num) {
		BattleDrop drop = this.drops.get(item);
		if (drop == null) {
			drop = new BattleDrop();
			drop.setItem(item);
			drops.put(item, drop);
		}
		drop.setNum(drop.getNum() + num);
	}

	public void addKill(int monsterId) {
		Integer num = this.kills.getOrDefault(monsterId, 0);
		this.kills.put(monsterId, num + 1);
	}

	public Map<Integer, BattleDrop> getDrops() {
		return drops;
	}

	public IBuffConfigCache getBuffConfigCache() {
		return buffConfigCache;
	}

	public IBattleType getBattleType() {
		return battleType;
	}

	public Map<Integer, Integer> getKills() {
		return kills;
	}

	public HarvestThing getHarvest(int zoneId, int harvestId) {
		Zone zone = zoneMap.get(zoneId);
		if (zone == null) {
			return null;
		}
		return zone.getHarvests().get(harvestId);
	}

	public long getNow() {
		return DateTimeUtil.currMillis();
	}

	public AtomicInteger getBuffIdGen() {
		return buffIdGen;
	}

	public BattleTeam getAtkTeam() {
		return atkTeam;
	}

	public BattleTeam getDefTeam() {
		return defTeam;
	}

	public void logInfo(String message, Object... params) {
		if (log) {
			logger.info(message, params);
		}
	}

	public void logError(String message, Object... params) {
		if (log) {
			logger.info(message, params);
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public BattleResult getBattleResult() {
		return battleResult;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public boolean isAtkTeamAllDead() {
		boolean allDead = true;
		for (Sprite sprite : this.getAtkTeam().getAllSpriteMap().values()) {
			if (sprite.isAlive()) {
				allDead = false;
				break;
			}
			if (this.battleProcessor.canRebornWithTime()) {
				if (this.getNow() >= sprite.getRebornTime()) {
					sprite.setAlive(true);
					allDead = false;
					break;
				}
			}
		}
		return allDead;
	}

	public boolean isDefTeamAllDead() {
		boolean allDead = true;
		for (Sprite sprite : this.getDefTeam().getAllSpriteMap().values()) {
			if (sprite.isAlive()) {
				allDead = false;
				break;
			}
			if (this.battleProcessor.canRebornWithTime()) {
				if (this.getNow() >= sprite.getRebornTime()) {
					sprite.setAlive(true);
					allDead = false;
					break;
				}
			}
		}
		return allDead;
	}
	
	public List<Integer> getAliveDefSprites() {
		List<Integer> list = new ArrayList<>();
		for (Sprite sprite : this.getDefTeam().getAllSpriteMap().values()) {
			if (sprite.isAlive()) {
				list.add(sprite.getId());
				continue;
			}
			if (this.battleProcessor.canRebornWithTime()) {
				if (this.getNow() >= sprite.getRebornTime()) {
					list.add(sprite.getId());
				}
			}
		}
		return list;
	}

	public int getMainHeroIdentity() {
		return mainHeroIdentity;
	}

	public void setMainHeroIdentity(int mainHeroIdentity) {
		this.mainHeroIdentity = mainHeroIdentity;
	}

	public Map<Integer, Zone> getZoneMap() {
		return zoneMap;
	}

	public void burySprite(Sprite sprite) {
		allSprites.remove(sprite.getId());
		if (sprite.getSide() == TeamSide.ATK) {
			atkTeam.burySprite(sprite);
		} else {
			defTeam.burySprite(sprite);
		}
	}

	public Object getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(Object extraParam) {
		this.extraParam = extraParam;
	}

	public ByteString getClientParams() {
		return clientParams;
	}

	public void setClientParams(ByteString clientParams) {
		this.clientParams = clientParams;
	}
}
