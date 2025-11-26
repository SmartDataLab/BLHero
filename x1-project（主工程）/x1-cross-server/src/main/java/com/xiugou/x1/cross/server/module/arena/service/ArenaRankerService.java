/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.service;

import java.util.List;

import org.gaming.db.repository.QueryOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.service.AbstractService;
import com.xiugou.x1.cross.server.module.arena.model.ArenaRanker;
import com.xiugou.x1.cross.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.module.player.model.PlayerSnapshot;

/**
 * @author hyy
 *
 */
@Service
public class ArenaRankerService extends AbstractService<ArenaRanker> {

	@Autowired
	private PlayerService playerService;
	
	public ArenaRanker getRanker(int crossGroup, int season, long playerId, int serverId) {
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.put("crossGroup", crossGroup);
		queryOptions.put("season", season);
		queryOptions.put("playerId", playerId);
		ArenaRanker ranker = this.repository().get(queryOptions);
		if(ranker == null) {
			ranker = new ArenaRanker();
			ranker.setCrossGroup(crossGroup);
			ranker.setSeason(season);
			ranker.setPlayerId(playerId);
			ranker.setServerId(serverId);
			PlayerSnapshot playerSnapshot = playerService.getPlayer(playerId, serverId);
			if(playerSnapshot != null) {
				ranker.setNick(playerSnapshot.getNick());
			}
			ranker.setScore(1000);
			ranker.setRobot(false);
			this.insert(ranker);
		}
		return ranker;
	}
	
	public List<ArenaRanker> getRankers(int crossGroup, int season) {
		QueryOptions options = new QueryOptions();
		options.put("crossGroup", crossGroup);
		options.put("season", season);
		List<ArenaRanker> rankers = repository().getList(options);
		return rankers;
	}
	
	public void updateAll(List<ArenaRanker> rankers) {
		this.repository().updateAll(rankers);
	}
	
	public void insertAll(List<ArenaRanker> rankers) {
		this.repository().insertAll(rankers);
	}
}
