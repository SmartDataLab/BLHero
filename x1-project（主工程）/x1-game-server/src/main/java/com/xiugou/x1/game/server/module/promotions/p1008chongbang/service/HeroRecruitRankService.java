/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang.service;

import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.promotions.p1008chongbang.event.HeroRecruitRankEvent;
import com.xiugou.x1.game.server.module.promotions.p1008chongbang.model.ChongBang;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;

/**
 * @author YY
 *
 */
@Service
public class HeroRecruitRankService extends PlayerRankService {

	@Autowired
	private ChongBangService chongBangService;
	
	@Override
	public RankType rankType() {
		return RankType.HERO_RECRUIT;
	}
	
	@Override
	protected boolean initWhenMyRankNotFound(long playerId) {
		ChongBang entity = chongBangService.getEntity(playerId, rankType().getActivityId());
		if(entity.getScore() <= 0) {
			return false;
		}
		updateRank(playerId, entity.getScore(), 0);
		return true;
	}
	
	@Subscribe
	private void listen(HeroRecruitRankEvent event) {
		ChongBang entity = chongBangService.getEntity(event.getPlayerId(), rankType().getActivityId());
		updateRank(entity.getPid(), entity.getScore(), 0);
	}
}
