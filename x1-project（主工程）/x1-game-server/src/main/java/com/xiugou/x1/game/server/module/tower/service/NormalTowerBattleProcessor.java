/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.model.Tower;

/**
 * @author YY
 *
 */
@Service
public class NormalTowerBattleProcessor extends BaseTowerBattleProcessor {

	@Override
	public IBattleType battleType() {
		return BattleType.TOWER;
	}

	@Override
	public TowerType towerType() {
		return TowerType.NORMAL;
	}

	@Override
	public int updateTowerLayer(Tower tower) {
		tower.setNormalLayer(tower.getNormalLayer() + 1);
		return tower.getNormalLayer();
	}
	
	@Override
	public int getTowerLayer(Tower tower) {
		return tower.getNormalLayer();
	}

	@Override
	public boolean checkOptionDate() {
		return true;
	}
}
