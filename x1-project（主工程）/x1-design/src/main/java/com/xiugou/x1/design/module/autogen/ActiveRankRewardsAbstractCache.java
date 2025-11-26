package com.xiugou.x1.design.module.autogen;


public abstract class ActiveRankRewardsAbstractCache<T extends ActiveRankRewardsAbstractCache.ActiveRankRewardsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "H活动-排行榜奖励配置表_ActiveRankRewards";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> activeIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引activeIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> activeIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = activeIdCollector.get(data.getActiveId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				activeIdCollector.put(data.getActiveId(), collector);
			}
			collector.add(data);
		}
		this.activeIdCollector = activeIdCollector;
	}



	public final java.util.ArrayList<T> getInActiveIdCollector(int activeId) {
		java.util.ArrayList<T> ts = activeIdCollector.get(activeId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ActiveRankRewardsCache.getInActiveIdCollector", activeId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInActiveIdCollector(int activeId) {
		java.util.ArrayList<T> ts = activeIdCollector.get(activeId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ActiveRankRewardsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 活动id
		 */
		protected int activeId;
		/**
		 * 排名上限
		 */
		protected int rankUp;
		/**
		 * 排名下限
		 */
		protected int rankDown;
		/**
		 * 排名奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getActiveId() {
			return activeId;
		}
		public int getRankUp() {
			return rankUp;
		}
		public int getRankDown() {
			return rankDown;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}