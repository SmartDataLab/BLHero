package com.xiugou.x1.game.server.module.purgatory.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.purgatory.event.PurgatoryLevelEvent;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;


/**
 * @author yh
 * @date 2023/8/10
 * @apiNote
 */
@Service
public class PurgatoryRankService extends PlayerRankService {
    @Autowired
    private PurgatoryService purgatoryService;
	
    @Override
    public RankType rankType() {
        return RankType.PURGATORY;
    }

    @Subscribe
    private void listen(PurgatoryLevelEvent event) {
		Purgatory purgatory = purgatoryService.getEntity(event.getEntityId());
		if (purgatory.getLevel() < event.getLevel()) {
			updateRank(event.getPid(), event.getLevel(), 0);
		}
    }

	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Purgatory purgatory = purgatoryService.getEntity(playerId);
		if(purgatory.getLevel() <= 0) {
			return false;
		}
		updateRank(playerId, purgatory.getLevel(), 0);
		return true;
	}
}
