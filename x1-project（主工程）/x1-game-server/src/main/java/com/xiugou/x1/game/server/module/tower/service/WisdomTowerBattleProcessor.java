/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.service;

import java.util.List;

import org.gaming.tool.LocalDateTimeUtil;
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
public class WisdomTowerBattleProcessor extends BaseTowerBattleProcessor {

	@Override
	public IBattleType battleType() {
		return BattleType.TOWER_OF_WISDOM;
	}

	@Override
	public TowerType towerType() {
		return TowerType.WISDOM;
	}

	@Override
	public int updateTowerLayer(Tower tower) {
		tower.setWisdomLayer(tower.getWisdomLayer() + 1);
		return tower.getWisdomLayer();
	}
	
	@Override
	public int getTowerLayer(Tower tower) {
		return tower.getWisdomLayer();
	}

	@Override
	public boolean checkOptionDate() {
		int value = LocalDateTimeUtil.now().getDayOfWeek().getValue();
		List<Integer> towerAgility = battleConstCache.getTower_wisdom();
		return towerAgility.contains(value);
	}
}
