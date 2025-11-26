/**
 * 
 */
package com.xiugou.x1.game.server.module.tower;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.model.Tower;
import com.xiugou.x1.game.server.module.tower.service.TowerService;

import pb.xiugou.x1.protobuf.tower.Tower.TowerInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class TowerGmHandler {

	@Autowired
	private TowerService towerService;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@PlayerGmCmd(command = "TOWER_LAYER")
	public void towerLayer(PlayerContext playerContext, String[] params) {
		int towerType = Integer.parseInt(params[0]);
		int towerLayer = Integer.parseInt(params[1]);
		
		Tower tower = towerService.getEntity(playerContext.getId());
		if(towerType == TowerType.NORMAL.getValue()) {
			tower.setNormalLayer(towerLayer);
		} else if(towerType == TowerType.STRENGTH.getValue()) {
			tower.setStrengthLayer(towerLayer);
		} else if(towerType == TowerType.AGILITY.getValue()) {
			tower.setAgilityLayer(towerLayer);
		} else if(towerType == TowerType.WISDOM.getValue()) {
			tower.setWisdomLayer(towerLayer);
		}
		towerService.update(tower);
		
		TowerInfoResponse.Builder response = TowerInfoResponse.newBuilder();
		response.setNormalLayer(tower.getNormalLayer());
		response.setStrengthLayer(tower.getStrengthLayer());
		response.setAgilityLayer(tower.getAgilityLayer());
		response.setWisdomLayer(tower.getWisdomLayer());
		playerContextManager.push(playerContext.getId(), TowerInfoResponse.Proto.ID, response.build());
	}
}
