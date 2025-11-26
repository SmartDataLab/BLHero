/**
 *
 */
package com.xiugou.x1.game.server.module.hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.HeroAwakenCache;
import com.xiugou.x1.design.module.HeroLevelCache;
import com.xiugou.x1.design.module.HeroLevelCache.HeroLevelConfig;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.module.autogen.HeroAwakenAbstractCache.HeroAwakenCfg;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.hero.event.HeroAwakenEvent;
import com.xiugou.x1.game.server.module.hero.event.HeroUpLevelEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.hero.Hero.HeroAwakenRequest;
import pb.xiugou.x1.protobuf.hero.Hero.HeroAwakenResponse;
import pb.xiugou.x1.protobuf.hero.Hero.HeroLevelUpRequest;
import pb.xiugou.x1.protobuf.hero.Hero.HeroLevelUpResponse;
import pb.xiugou.x1.protobuf.hero.Hero.HeroListResponse;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbItem;

/**
 * @author YY
 */
@Controller
public class HeroHandler extends AbstractModuleHandler {

	@Autowired
	private HeroService heroService;
	@Autowired
	private HeroLevelCache heroLevelCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private HeroTypeCache heroTypeCache;
	@Autowired
	private HeroAwakenCache heroAwakenCache;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.BASE;
	}

	@Override
	public boolean needDailyPush() {
		return false;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		Map<Integer, Hero> heroes = heroService.getHeroMap(playerId);
		HeroListResponse.Builder response = HeroListResponse.newBuilder();
		for (HeroTypeConfig config : heroTypeCache.all()) {
			Hero hero = heroes.get(config.getId());
			if (hero != null) {
				response.addHeroes(PbHelper.build(hero));
			} else {
				response.addUnactiveHeroes(heroService.getDefaultHero(config.getId()));
			}
		}
		playerContextManager.push(playerId, HeroListResponse.Proto.ID, response.build());
	}

	@PlayerCmd
	public HeroLevelUpResponse levelUp(PlayerContext playerContext, HeroLevelUpRequest request) {
		Hero hero = heroService.getEntity(playerContext.getId(), request.getIdentity());
		Asserts.isTrue(hero != null, TipsCode.HERO_NOT_EXIST, request.getIdentity());

		int level = hero.getLevel(); // 英雄当前等级
		HeroLevelConfig heroLevelConfig = heroLevelCache.getInHeroIdentityLevelIndex(request.getIdentity(), level + 1);

		List<CostThing> costThings = new ArrayList<>(heroLevelConfig.getCostMaterial());
		costThings.add(CostThing.of(request.getIdentity(), heroLevelConfig.getCostBody()));

		thingService.cost(playerContext.getId(), costThings, GameCause.HERO_UP_LEVEL, level + "->" + (level + 1));

		hero.setLevel(level + 1);
		heroService.calculateAttr(hero, GameCause.HERO_UP_LEVEL);

		// 英雄升级事件
		EventBus.post(HeroUpLevelEvent.of(playerContext.getId(), request.getIdentity()));

		HeroLevelUpResponse.Builder response = HeroLevelUpResponse.newBuilder();
		response.setHero(PbHelper.build(hero));
		return response.build();
	}

	@PlayerCmd
	public HeroAwakenResponse awaken(PlayerContext playerContext, HeroAwakenRequest request) {
		Hero hero = heroService.getEntity(playerContext.getId(), request.getIdentity());
		Asserts.isTrue(hero != null, TipsCode.HERO_NOT_EXIST, request.getIdentity());

		HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(hero.getIdentity());
		HeroAwakenCfg heroAwakenCfg = heroAwakenCache.getInQualityLevelIndex(heroTypeCfg.getQuality(),
				hero.getAwakenLevel() + 1);

		Asserts.isTrue(hero.getFragment() >= heroAwakenCfg.getNeedBody(), TipsCode.HERO_FRAGMENT_LACK);

		Map<Integer, CostThing> costMap = new HashMap<>();
		costMap.put(hero.getIdentity(), CostThing.of(hero.getIdentity(), heroAwakenCfg.getNeedBody()));
		
		Set<Integer> chooseHeros = new HashSet<>();
		long totalNum = 0;
		for (PbItem pbItem : request.getItemsList()) {
			Asserts.isTrue(!chooseHeros.contains(pbItem.getItem()), TipsCode.HERO_SAME_HERO_PIECE);
			Hero otherHero = heroService.getEntity(playerContext.getId(), pbItem.getItem());
			Asserts.isTrue(otherHero != null, TipsCode.HERO_NOT_EXIST, pbItem.getItem());
			HeroTypeCfg otherHeroTypeCfg = heroTypeCache.getOrThrow(otherHero.getIdentity());
			Asserts.isTrue(otherHeroTypeCfg.getQuality() == heroAwakenCfg.getOtherQuality(), TipsCode.HERO_QUALITY_NOT_MATCH);
			// 素材英雄可以用本体英雄来代替，因此需要在计算碎片消耗时加上本体英雄已消耗的碎片数
			CostThing oriBodyCost = costMap.get(otherHero.getIdentity());
			long oriBodyCostNum = 0;
			if (oriBodyCost != null) {
				oriBodyCostNum = oriBodyCost.getNum();
			}
			Asserts.isTrue(otherHero.getFragment() >= pbItem.getNum() + oriBodyCostNum,
					TipsCode.HERO_SUB_FRAGMENT_LACK);
			chooseHeros.add(otherHero.getIdentity());
			costMap.put(otherHero.getIdentity(),
					CostThing.of(otherHero.getIdentity(), pbItem.getNum() + oriBodyCostNum));
			totalNum += pbItem.getNum();
		}
		Asserts.isTrue(totalNum >= heroAwakenCfg.getOtherBody(), TipsCode.HERO_SUB_FRAGMENT_LACK);

		int awakenLevel = hero.getAwakenLevel();
		List<CostThing> costList = new ArrayList<>(costMap.values());
		costList.addAll(heroAwakenCfg.getCost());
		thingService.cost(playerContext.getId(), costList, GameCause.HERO_AWAKEN, awakenLevel + "->" + (awakenLevel + 1));

		hero.setAwakenLevel(hero.getAwakenLevel() + 1);
		heroService.calculateAttr(hero, GameCause.HERO_AWAKEN);

		// 英雄升级事件
		EventBus.post(HeroAwakenEvent.of(playerContext.getId(), hero.getIdentity()));

		HeroAwakenResponse.Builder response = HeroAwakenResponse.newBuilder();
		response.setHero(PbHelper.build(hero));
		return response.build();
	}
}
