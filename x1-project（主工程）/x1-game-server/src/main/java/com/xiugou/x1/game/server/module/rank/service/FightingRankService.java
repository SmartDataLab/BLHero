package com.xiugou.x1.game.server.module.rank.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.mainline.event.MainlineFormationChangeEvent;
import com.xiugou.x1.game.server.module.rank.constant.RankType;

/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
@Service
public class FightingRankService extends PlayerRankService {
	
	@Autowired
	private FormationService formationService;

	@Override
	public RankType rankType() {
		return RankType.FIGHTING;
	}

	@Subscribe
	private void listen(MainlineFormationChangeEvent event) {
		Formation formation = formationService.getEntity(event.getPid(), BattleType.MAINLINE);
		updateRank(event.getPid(), formation.getFighting(), 0);
		updateRankReward(event.getPid(), formation.getFighting());
	}
	
	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Formation formation = formationService.getEntity(playerId, BattleType.MAINLINE);
		updateRank(playerId, formation.getFighting(), 0);
		updateRankReward(playerId, formation.getFighting());
		return true;
	}
}
