/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.service;

import java.util.List;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.model.Tower;

/**
 * @author YY
 *
 */
@Service
public class AgilityTowerBattleProcessor extends BaseTowerBattleProcessor {
	@Autowired
	private BattleConstCache battleConstCache;

	@Override
	public IBattleType battleType() {
		return BattleType.TOWER_OF_AGILITY;
	}

	@Override
	public TowerType towerType() {
		return TowerType.AGILITY;
	}

	@Override
	public int updateTowerLayer(Tower tower) {
		tower.setAgilityLayer(tower.getAgilityLayer() + 1);
		return tower.getAgilityLayer();
	}

	@Override
	public int getTowerLayer(Tower tower) {
		return tower.getAgilityLayer();
	}

	@Override
	public boolean checkOptionDate() {
		int value = LocalDateTimeUtil.now().getDayOfWeek().getValue();
		List<Integer> towerAgility = battleConstCache.getTower_agility();
		return towerAgility.contains(value);
	}
}
