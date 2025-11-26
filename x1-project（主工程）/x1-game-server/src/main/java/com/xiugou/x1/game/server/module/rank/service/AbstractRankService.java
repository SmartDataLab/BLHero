/**
 *
 */
package com.xiugou.x1.game.server.module.rank.service;

import java.util.List;

import org.gaming.db.repository.CacheRepository;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.rank.top.TopComparator;
import org.gaming.prefab.rank.top.TopList;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.model.Ranker;

import pb.xiugou.x1.protobuf.rank.Rank.PbRanker;

/**
 * @author YY
 */
public abstract class AbstractRankService implements Lifecycle {

	private static Logger logger = LoggerFactory.getLogger(AbstractRankService.class);


	private static TopComparator<Ranker> SORTER = new TopComparator<Ranker>() {
		@Override
		public int compara(Ranker o1, Ranker o2) {
			int result = Long.compare(o2.getScore(), o1.getScore());
			if (result != 0) {
				return result;
			}
			result = Long.compare(o2.getSubScore(), o1.getSubScore());
			if (result != 0) {
				return result;
			}
			result = Long.compare(o1.getTime(), o2.getTime());
			if (result != 0) {
				return result;
			}
			return Long.compare(o1.getEntityId(), o2.getEntityId());
		}

		@Override
		public int sdscmp(Ranker o1, Ranker o2) {
			return Long.compare(o1.getEntityId(), o2.getEntityId());
		}
	};

	//topN中的Ranker对象跟CacheRepository中的Ranker对象会出现不是同一个对象的情况
	private TopList<Ranker> topN;
	private CacheRepository<Ranker> repository;

	protected CacheRepository<Ranker> repository() {
		if (repository == null) {
			repository = SlimDao.getCacheRepository(Ranker.class);
		}
		return repository;
	}

	public abstract RankType rankType();

	public TopComparator<Ranker> getComparator() {
		return SORTER;
	}

	/**
	 * 清理排行榜
	 */
	public void clearRank() {
		topN.clear();
	}
	
	/**
	 * 清理排行榜并删除排行榜数据
	 */
	public void clearRankAndDb() {
		List<Ranker> rankers = getAll();
		repository().deleteAll(rankers);
		topN.clear();
	}
	
	public List<Ranker> getAll() {
		return topN.getElementsByRange(1, topN.getLength());
	}

	@Override
	public void start() throws Exception {
		QueryOptions options = new QueryOptions();
		options.put("rankType", this.rankType().getValue());
		List<Ranker> rankers = repository().getList(options);

		this.topN = new TopList<>(this.getComparator(), this.rankType().getLimit());
		for (Ranker ranker : rankers) {
			topN.insert(ranker);
		}
		logger.info("起服加载排行榜{}数据数量{}", this.rankType(), rankers.size());
	}

	/**
	 * 根据实体ID获取排名对象
	 *
	 * @param entityId
	 * @return
	 */
	public Ranker getRanker(long entityId) {
		return this.repository().getByMainKey(this.rankType() + "_" + entityId);
	}

	/**
	 * 根据实体对象获取排名名次
	 *
	 * @param ranker
	 * @return
	 */
	public int getRank(Ranker ranker) {
		return topN.getRank(ranker);
	}

	/**
	 * 根据实体ID获取排名名次
	 *
	 * @param entityId
	 * @return
	 */
	public int getRank(long entityId) {
		Ranker ranker = this.getRanker(entityId);
		if (ranker == null) {
			return 0;
		}
		return topN.getRank(ranker);
	}

	/**
	 * 获取从rankMin到rankMax排名的数据，rankMin从1开始
	 *
	 * @param rankMin
	 * @param rankMax
	 * @return
	 */
	public List<Ranker> getTopN(int rankMin, int rankMax) {
		return topN.getElementsByRange(rankMin, rankMax);
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
	public synchronized void updateRank(long entityId, long score, long subScore) {
		Ranker ranker = this.getRanker(entityId);
		if (ranker != null) {
			topN.delete(ranker);

			ranker.setScore(score);
			ranker.setSubScore(subScore);
			ranker.setTime(DateTimeUtil.currMillis());
			this.repository().update(ranker);
		} else {
			ranker = new Ranker();
			ranker.setId(rankType() + "_" + entityId);
			ranker.setEntityId(entityId);
			ranker.setName(getEntityName(entityId));
			ranker.setScore(score);
			ranker.setSubScore(subScore);
			ranker.setRankType(this.rankType().getValue());
			ranker.setRankDesc(this.rankType().getDesc());
			ranker.setTime(DateTimeUtil.currMillis());
			this.repository().insert(ranker);
		}
		topN.insert(ranker);
	}


	/**
	 * 根据实体ID获取实体名字
	 *
	 * @param entityId
	 * @return
	 */
	public abstract String getEntityName(long entityId);

	/**
	 * 序列化排名列表数据
	 *
	 * @param ranker
	 * @param rank   名次
	 * @return
	 */
	public abstract List<PbRanker> buildList(int page, int pageSize);

	/**
	 * 序列化当前实体的排名数据
	 *
	 * @param entityId
	 * @return
	 */
	public abstract PbRanker buildSelf(long entityId);
}
