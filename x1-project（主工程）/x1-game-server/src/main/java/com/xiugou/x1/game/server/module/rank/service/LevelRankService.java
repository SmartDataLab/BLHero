package com.xiugou.x1.game.server.module.rank.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.player.event.PlayerUpLevelEvent;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.rank.constant.RankType;



/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
@Service
public class LevelRankService extends PlayerRankService {

	@Override
	public RankType rankType() {
		return RankType.LEVEL;
	}

	@Subscribe
	private void listen(PlayerUpLevelEvent event) {
		Player player = event.getPlayer();
		updateRank(player.getId(), player.getLevel(), 0);
		updateRankReward(player.getId(), player.getLevel());
	}
	
	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		Player player = playerService.getEntity(playerId);
		updateRank(player.getId(), player.getLevel(), 0);
		updateRankReward(player.getId(), player.getLevel());
		return true;
	}
}
