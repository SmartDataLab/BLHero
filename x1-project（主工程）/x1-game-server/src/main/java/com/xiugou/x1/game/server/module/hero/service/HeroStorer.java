/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.CostReceipt;
import org.gaming.prefab.thing.ICostThing;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.NumberThingStorer;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.game.server.module.hero.event.GetNewHeroEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.hero.HeroLog;

/**
 * @author YY
 *
 */
@Component
public class HeroStorer extends NumberThingStorer<HeroLog> {

	@Autowired
	private HeroService heroService;
	@Autowired
	private HeroTypeCache heroTypeCache;
	@Autowired
    private PlayerService playerService;
	
	@Override
	protected IThingType thingType() {
		return ItemType.HERO;
	}

	@Override
	protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
		Map<Integer, Hero> heroMap = heroService.getHeroMap(entityId);
		
		RewardReceipt receipt = new RewardReceipt();
		
		List<Hero> insertList = new ArrayList<>();
		List<Hero> updateList = new ArrayList<>();
		for(IRewardThing reward : rewardThings) {
			HeroTypeConfig heroCfg = heroTypeCache.getOrThrow(reward.getThingId());
			RewardDetail rewardDetail = null;
			Hero hero = heroMap.get(heroCfg.getId());
			if(hero == null) {
				hero = new Hero();
				hero.setPid(entityId);
				hero.setIdentity(heroCfg.getId());
				hero.setName(heroCfg.getName());
				hero.setLevel(1);
				heroService.calculateAndSetAttr(hero);
				hero.setFragment(hero.getFragment() + reward.getNum());
				hero.setHisFragment(hero.getHisFragment() + reward.getNum());
				insertList.add(hero);
				rewardDetail = new RewardDetail(reward.getThingId(), reward.getNum(), PbHelper.build(hero, true).toByteString());
			} else {
				hero.setFragment(hero.getFragment() + reward.getNum());
				hero.setHisFragment(hero.getHisFragment() + reward.getNum());
				updateList.add(hero);
				rewardDetail = new RewardDetail(reward.getThingId(), reward.getNum(), PbHelper.build(hero).toByteString());
			}
			receipt.append(rewardDetail);
		}
		heroService.insert(insertList);
		heroService.update(updateList);
		if(!insertList.isEmpty()) {
			EventBus.post(GetNewHeroEvent.of(entityId, insertList));
		}
		return receipt;
	}

	@Override
	protected ITipCause lackCode() {
		return TipsCode.HERO_FREGMENT_LACK;
	}

	@Override
	public long getCount(long entityId, int thingId) {
		Hero hero = heroService.getEntity(entityId, thingId);
		if(hero == null) {
			return 0;
		} else {
			return hero.getFragment();
		}
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
		Map<Integer, Hero> heroMap = heroService.getHeroMap(entityId);
		
		List<Hero> updateList = new ArrayList<>();
		for(ICostThing cost : costThings) {
			Hero hero = heroMap.get(cost.getThingId());
			hero.setFragment(hero.getFragment() - cost.getNum());
			updateList.add(hero);
		}
		heroService.update(updateList);
		
		return new CostReceipt(costThings);
	}

	@Override
	protected HeroLog newLog() {
		return new HeroLog();
	}

	@Override
	protected BaseRepository<HeroLog> initRepository() {
		return SlimDao.getRepository(HeroLog.class);
	}

	@Override
	protected String thingName(int thingId) {
		return heroTypeCache.getOrThrow(thingId).getName();
	}

	@Override
	protected String getOwnerName(long entityId) {
		return playerService.getEntity(entityId).getNick();
	}
}
