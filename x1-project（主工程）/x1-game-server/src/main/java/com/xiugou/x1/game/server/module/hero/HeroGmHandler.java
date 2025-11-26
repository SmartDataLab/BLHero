/**
 * 
 */
package com.xiugou.x1.game.server.module.hero;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.HeroLevelCache;
import com.xiugou.x1.design.module.HeroLevelCache.HeroLevelConfig;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.hero.event.HeroUpLevelEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;

/**
 * @author YY
 *
 */
@Controller
public class HeroGmHandler {

	@Autowired
	private HeroTypeCache heroTypeCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private HeroLevelCache heroLevelCache;
	
	@PlayerGmCmd(command = "ALL_HERO")
	public void allHero(PlayerContext playerContext, String[] params) {
		List<RewardThing> rewardThings = new ArrayList<>();
		for(HeroTypeConfig cfg : heroTypeCache.all()) {
			rewardThings.add(RewardThing.of(cfg.getId(), 100));
		}
		thingService.add(playerContext.getId(), rewardThings, GameCause.GM);
	}
	
	@PlayerGmCmd(command = "TEST_FIGHTING")
	public void calculateFighting(PlayerContext playerContext, String[] params) {
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.GM);
		for(Hero hero : heroService.getEntities(playerContext.getId())) {
			System.out.println(hero.getName() + " " + GsonUtil.toJson(hero.getPanelAttr().notZeroAttrMap()));
		}
		
//		BattleConstCache battleConstCache = Spring.getBean(BattleConstCache.class);
		
//		BattleAttr battleAttr = new BattleAttr();
//		battleAttr.setMaxHp(4433);
//		battleAttr.setAtk(4391);
//		battleAttr.setHpRate(100);
//		battleAttr.setAtkRate(300);
//		battleAttr.setHit(10000);
//		battleAttr.setDodge(2700);
//		battleAttr.setCrit(1300);
//		battleAttr.setCritDmgRate(15000);
//		battleAttr.setReduceDmg(17);
//		battleAttr.setReduceDmgRate(400);
//		battleAttr.setCdSpdRate(300);
//		battleAttr.setMoveSpd(572);
//		battleAttr.setMoveSpdRate(400);
//		battleAttr.setAtkSpd(500);
//		battleAttr.setDmg(2);
//		battleAttr.setWoodDmg(1);
//		battleAttr.setMineDmg(1);
//		battleAttr.setBattleRange(600);
//		battleAttr.setLlpvedmg(600);
//		for (Attr attr : battleConstCache.getHero_init_attr()) {
//			battleAttr.subById(attr.getAttrId(), attr.getValue());
//        }
//		System.out.println(battleAttr.notZeroAttrMap());
//		System.out.println("sdsda " + heroService.calculateFighting(battleAttr));
	}
	
	@PlayerGmCmd(command = "HERO_MAXLV")
	public void heroMaxLevel(PlayerContext playerContext, String[] params) {
		List<Hero> heroes = heroService.getEntities(playerContext.getId());
		Hero upHero = null;
		for(Hero hero : heroes) {
			List<HeroLevelConfig> cfgList = heroLevelCache.getInHeroIdentityCollector(hero.getIdentity());
			int maxLevel = 0;
			for(HeroLevelConfig config : cfgList) {
				if(config.getLevel() > maxLevel) {
					maxLevel = config.getLevel();
				}
			}
			hero.setLevel(maxLevel);
			upHero = hero;
		}
		heroService.update(heroes);
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.GM);
		
		if(upHero != null) {
			EventBus.post(HeroUpLevelEvent.of(playerContext.getId(), upHero.getIdentity()));
		}
	}
	
	//增加当前拥有英雄的碎片
	@PlayerGmCmd(command = "ADD_CURR_HERO")
	public void addCurrHero(PlayerContext playerContext, String[] params) {
		List<Hero> heros = heroService.getEntities(playerContext.getId());
		List<RewardThing> rewardThings = new ArrayList<>();
		for(Hero hero : heros) {
			rewardThings.add(RewardThing.of(hero.getIdentity(), 500));
		}
		thingService.add(playerContext.getId(), rewardThings, GameCause.GM);
	}
}
