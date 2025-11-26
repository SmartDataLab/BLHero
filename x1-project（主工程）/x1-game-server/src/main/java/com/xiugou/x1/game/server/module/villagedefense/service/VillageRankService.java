package com.xiugou.x1.game.server.module.villagedefense.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;
import com.xiugou.x1.game.server.module.villagedefense.event.VillageLevelEvent;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;

/**
 * @author yh
 * @date 2023/8/17
 * @apiNote
 */
@Service
public class VillageRankService extends PlayerRankService {

	@Autowired
	private VillageService villageService;

	@Override
	public RankType rankType() {
		return RankType.VILLAGE;
	}

	@Subscribe
	private void listen(VillageLevelEvent event) {
		Village village = villageService.getEntity(event.getEntityId());
		if (village.getLevel() < event.getLevel()) {
			updateRank(event.getPid(), event.getLevel(), 0);
		}
	}

	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Village village = villageService.getEntity(playerId);
		if(village.getLevel() <= 0) {
			return false;
		}
		updateRank(playerId, village.getLevel(), 0);
		return true;
	}
}
