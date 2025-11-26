/**
 * 
 */
package com.xiugou.x1.game.server.module.rank.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.event.TowerWinEvent;
import com.xiugou.x1.game.server.module.tower.model.Tower;
import com.xiugou.x1.game.server.module.tower.service.TowerService;

/**
 * @author YY
 *
 */
@Service
public class TowerNormalRankService extends PlayerRankService {

	@Autowired
	private TowerService towerService;

	@Override
	public RankType rankType() {
		return RankType.TOWER_NORMAL;
	}

	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Tower tower = towerService.getEntity(playerId);
		long totalScore = tower.getNormalLayer();
		if (totalScore <= 0) {
			return false;
		}
		updateRank(playerId, totalScore, 0);
		updateRankReward(playerId, totalScore);
		return true;
	}

	@Subscribe
	private void listen(TowerWinEvent event) {
		if(event.getTowerType() != TowerType.NORMAL) {
			return;
		}
		Tower tower = towerService.getEntity(event.getPlayerId());
		updateRank(tower.getPid(), tower.getNormalLayer(), 0);
		updateRankReward(tower.getPid(), tower.getNormalLayer());
	}

}
