package com.xiugou.x1.game.server.module.rank.service;

import java.util.List;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.equip.event.EquipWearChangEvent;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.service.EquipWearService;
import com.xiugou.x1.game.server.module.rank.constant.RankType;

/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
@Service
public class EquipRankService extends PlayerRankService {
	
	@Autowired
	private EquipWearService equipWearService;

	@Override
	public RankType rankType() {
		return RankType.EQUIP;
	}

	@Subscribe
	private void listen(EquipWearChangEvent event) {
		List<Equip> wearingEquipList = equipWearService.getWearingEquipList(event.getEntityId());
		long totalScore = wearingEquipList.stream().mapToLong(Equip::getScore).reduce(0, Long::sum);
		if(totalScore <= 0) {
			return;
		}
		updateRank(event.getEntityId(), totalScore, 0);
		updateRankReward(event.getEntityId(), totalScore);
	}

	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		List<Equip> wearingEquipList = equipWearService.getWearingEquipList(playerId);
		long totalScore = wearingEquipList.stream().mapToLong(Equip::getScore).reduce(0, Long::sum);
		if(totalScore <= 0) {
			return false;
		}
		updateRank(playerId, totalScore, 0);
		updateRankReward(playerId, totalScore);
		return true;
	}
}
