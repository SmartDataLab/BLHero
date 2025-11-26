/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.service;

import java.util.List;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.mainline.event.FogOpenEvent;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;

/**
 * @author YY
 *
 */
@Service
public class MainlineFogRankService extends PlayerRankService {

	@Autowired
	private MainlineSceneService mainlineSceneService;
	
	@Override
	public RankType rankType() {
		return RankType.FOG;
	}

	@Subscribe
	private void listen(FogOpenEvent event) {
		MainlineScene mainlineScene = mainlineSceneService.getOrThrow(event.getEntityId(), event.getSceneId());
		if(mainlineScene.getFogs().size() <= 0) {
			return;
		}
		updateRank(event.getEntityId(), mainlineScene.getFogs().size(), 0);
		updateRankReward(event.getEntityId(), mainlineScene.getFogs().size());
	}

	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		List<MainlineScene> mainlineScenes = mainlineSceneService.getEntities(playerId);
		int score = 0;
		for(MainlineScene mainlineScene : mainlineScenes) {
			score += mainlineScene.getFogs().size();
		}
		if(score <= 0) {
			return false;
		}
		updateRank(playerId, score, 0);
		updateRankReward(playerId, score);
		return true;
	}
}
