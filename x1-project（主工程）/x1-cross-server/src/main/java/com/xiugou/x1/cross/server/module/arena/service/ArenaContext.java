/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.service;

import java.util.List;

import org.gaming.prefab.rank.top.TopComparator;
import org.gaming.prefab.rank.top.TopList;

import com.xiugou.x1.cross.server.module.arena.model.ArenaRanker;
import com.xiugou.x1.cross.server.module.arena.model.ArenaSeason;

/**
 * @author hyy
 *
 */
public class ArenaContext {
	private final ArenaSeason season;
	private final TopList<ArenaRanker> topN = new TopList<>(SORTER);
	
	private final ArenaRankerService arenaRankerService;
	
	public ArenaContext(ArenaSeason season, ArenaRankerService arenaRankerService) {
		this.season = season;
		this.arenaRankerService = arenaRankerService;
	}
	
	public int getCrossGroup() {
		return this.season.getId();
	}
	
	public int getCurrSeason() {
		return this.season.getSeason();
	}
	
	public void addRankers(List<ArenaRanker> rankers) {
		for(ArenaRanker ranker : rankers) {
			if(ranker.isRobot()) {
				this.topN.insert(ranker);
			} else if(ranker.isHasFight()) {
				this.topN.insert(ranker);
			}
		}
	}
	
	/**
	 * 根据实体ID获取排名对象
	 *
	 * @param entityId
	 * @return
	 */
	public ArenaRanker getRanker(long playerId, int serverId) {
		return arenaRankerService.getRanker(getCrossGroup(), getCurrSeason(), playerId, serverId);
	}
	
	/**
	 * 更新排行榜
	 * 先从排行榜中删除元素，再插入排行数据
	 *
	 * @param entityId
	 * @param score
	 * @param subScore
	 * @param time
	 */
	public synchronized void updateRank(long playerId, int serverId, long score, long subScore) {
		ArenaRanker ranker = this.getRanker(playerId, serverId);
		topN.delete(ranker);

		ranker.setScore(score);
		arenaRankerService.update(ranker);
		topN.insert(ranker);
	}
	
	/**
	 * 获取从rankMin到rankMax排名的数据，rankMin从1开始
	 *
	 * @param rankMin
	 * @param rankMax
	 * @return
	 */
	public List<ArenaRanker> getTopN(int rankMin, int rankMax) {
		return topN.getElementsByRange(rankMin, rankMax);
	}
	/**
	 * 获取所有排名
	 * @return
	 */
	public List<ArenaRanker> getAllTopN() {
		return topN.getElementsByRange(1, topN.getLength());
	}
	/**
	 * 根据实体对象获取排名名次
	 *
	 * @param ranker
	 * @return
	 */
	public int getRank(ArenaRanker ranker) {
		return topN.getRank(ranker);
	}
	/**
	 * 根据实体ID获取排名名次
	 *
	 * @param entityId
	 * @return
	 */
	public int getRank(long playerId, int serverId) {
		ArenaRanker ranker = this.getRanker(playerId, serverId);
		if (ranker == null) {
			return 0;
		}
		return topN.getRank(ranker);
	}
	/**
	 * 获取最大排名
	 * @return
	 */
	public int getMaxRank() {
		return topN.getLength();
	}
	
	public static TopComparator<ArenaRanker> SORTER = new TopComparator<ArenaRanker>() {
		@Override
		public int compara(ArenaRanker t1, ArenaRanker t2) {
			int result = Long.compare(t2.getScore(), t1.getScore());
			if(result != 0) {
				return result;
			}
			return Long.compare(t1.getPlayerId(), t2.getPlayerId());
		}

		@Override
		public int sdscmp(ArenaRanker t1, ArenaRanker t2) {
			return Long.compare(t1.getPlayerId(), t2.getPlayerId());
		}
	};

	public ArenaSeason getSeason() {
		return season;
	}
}
