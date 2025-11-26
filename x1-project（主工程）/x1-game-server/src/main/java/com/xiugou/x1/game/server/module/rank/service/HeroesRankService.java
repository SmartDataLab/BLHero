package com.xiugou.x1.game.server.module.rank.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.mainline.event.MainlineFormationChangeEvent;
import com.xiugou.x1.game.server.module.rank.constant.RankType;

/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
@Service
public class HeroesRankService extends PlayerRankService {
	@Autowired
	private FormationService formationService;
	@Autowired
	private HeroService heroService;

	@Override
	public RankType rankType() {
		return RankType.HERO;
	}

	@Subscribe
	private void listen(MainlineFormationChangeEvent event) {
		long entityId = event.getEntityId();
		Formation formation = formationService.getEntity(entityId, BattleType.MAINLINE);
		long fighting = 0;
		for (int identity : formation.getPositions().keySet()) {
			fighting += heroService.calculateInitFighting(entityId, identity);
		}
		updateRank(entityId, fighting, 0);
		updateRankReward(entityId, fighting);
	}
	
	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Formation formation = formationService.getEntity(playerId, BattleType.MAINLINE);
		long fighting = 0;
		for (int identity : formation.getPositions().keySet()) {
			fighting += heroService.calculateInitFighting(playerId, identity);
		}
		updateRank(playerId, fighting, 0);
		updateRankReward(playerId, fighting);
		return true;
	}
}
