package com.xiugou.x1.game.server.module.rank.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.rank.model.RankPlayer;

/**
 * @author yh
 * @date 2023/7/26
 * @apiNote
 */
@Service
public class RankPlayerService extends PlayerOneToOneService<RankPlayer> {
	
	@Override
	protected RankPlayer createWhenNull(long entityId) {
		RankPlayer rankPlayer = new RankPlayer();
		rankPlayer.setPid(entityId);
		return rankPlayer;
	}
}
