package com.xiugou.x1.game.server.module.villagedefense;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;
import com.xiugou.x1.game.server.module.villagedefense.service.VillageService;

/**
 * @author yh
 * @date 2023/10/26
 * @apiNote
 */
@Controller
public class VillageDefenseGmHandler {
	@Autowired
	private VillageService villageService;

	@PlayerGmCmd(command = "VilLAGE_SKIP_LEVEL")
	public void skipLevel(PlayerContext playerContext, String[] params) {
		Village village = villageService.getEntity(playerContext.getId());
		village.setLevel(Integer.parseInt(params[0]));
		villageService.update(village);
	}
}
