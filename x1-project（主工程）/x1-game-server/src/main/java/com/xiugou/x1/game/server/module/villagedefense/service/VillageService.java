package com.xiugou.x1.game.server.module.villagedefense.service;

import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;
import com.xiugou.x1.game.server.module.villagedefense.model.VillageSystem;

/**
 * @author yh
 * @date 2023/8/16
 * @apiNote
 */
@Service
public class VillageService extends PlayerOneToOneResetableService<Village> {

	@Autowired
	private BattleTypeCache battleTypeCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private VillageSystemService villageSystemService;
	@Autowired
	private BattleConstCache battleConstCache;

	@Override
	protected Village createWhenNull(long entityId) {
		Village village = new Village();
		village.setPid(entityId);
		village.setRound(1);
		return village;
	}

	@Override
	protected void doDailyReset(Village entity) {
		// 发免费门票 昨天的没用 今天不发
		if (entity.getChallengeTimes() > 0) {
			BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(BattleType.VILLAGE_DEFENSE.getValue());
			thingService.add(entity.getPid(), battleTypeCfg.getFreeTicket(), GameCause.VILLAGE_TICKET, NoticeType.SLIENT);
		}
		entity.setChallengeTimes(0);
	}

	@Override
	protected boolean doSpecialReset(Village entity) {
		VillageSystem villageSystem = villageSystemService.getEntity();
		if (entity.getRound() == villageSystem.getRound()) {
			return false;
		}
		int gap = villageSystem.getRound() - entity.getRound();
		int minus = battleConstCache.getVillage_break_level() * gap;
		entity.setRound(villageSystem.getRound());
		entity.setLevel(entity.getLevel() - minus);
		if (entity.getLevel() < 0) {
			entity.setLevel(0);
		}
		return true;
	}
}
