/**
 * 
 */
package com.xiugou.x1.game.server.module.tower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.tower.model.Tower;
import com.xiugou.x1.game.server.module.tower.service.TowerService;

import pb.xiugou.x1.protobuf.tower.Tower.TowerInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class TowerHandler extends AbstractModuleHandler {

	@Autowired
	private TowerService towerService;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public boolean needDailyPush() {
		return false;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		Tower tower = towerService.getEntity(playerId);
		
		TowerInfoResponse.Builder response = TowerInfoResponse.newBuilder();
		response.setNormalLayer(tower.getNormalLayer());
		response.setStrengthLayer(tower.getStrengthLayer());
		response.setAgilityLayer(tower.getAgilityLayer());
		response.setWisdomLayer(tower.getWisdomLayer());
		playerContextManager.push(playerId, TowerInfoResponse.Proto.ID, response.build());
	}
}
