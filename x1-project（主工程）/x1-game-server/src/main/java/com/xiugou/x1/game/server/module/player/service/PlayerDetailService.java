/**
 * 
 */
package com.xiugou.x1.game.server.module.player.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.player.model.PlayerDetail;

/**
 * @author YY
 *
 */
@Service
public class PlayerDetailService extends PlayerOneToOneService<PlayerDetail> {

	@Override
	protected PlayerDetail createWhenNull(long entityId) {
		PlayerDetail playerDetail = new PlayerDetail();
		playerDetail.setPid(entityId);
		return playerDetail;
	}
}
