package com.xiugou.x1.design.module.autogen;


public abstract class ZhanLingExpAbstractCache<T extends ZhanLingExpAbstractCache.ZhanLingExpCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1001经验类战令奖励_ZhanLingExp";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> activityIdLevelIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引activityIdLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> activityIdLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = activityIdLevelIndex.get(data.getActivityId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				activityIdLevelIndex.put(data.getActivityId(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.activityIdLevelIndex = activityIdLevelIndex;
		//构建索引activityIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = activityIdCollector.get(data.getActivityId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				activityIdCollector.put(data.getActivityId(), collector);
			}
			collector.add(data);
		}
		this.activityIdCollector = activityIdCollector;
	}

	public final T getInActivityIdLevelIndex(int activityId, int level) {
		java.util.HashMap<Integer, T> layer1Map = activityIdLevelIndex.get(activityId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhanLingExpCache.getInActivityIdLevelIndex", activityId, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhanLingExpCache.getInActivityIdLevelIndex", activityId, level);
		}
		return t;
	}

	public final T findInActivityIdLevelIndex(int activityId, int level) {
		java.util.HashMap<Integer, T> layer1Map = activityIdLevelIndex.get(activityId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhanLingExpCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ZhanLingExpCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 活动类型ID
		 */
		protected int activityId;
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 升至本级所需经验
		 */
		protected int needExp;
		/**
		 * 免费奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> freeReward;
		/**
		 * 高级奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> premiumReward;
		/**
		 * 购买本等级消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing buyLevelCost;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getActivityId() {
			return activityId;
		}
		public int getLevel() {
			return level;
		}
		public int getNeedExp() {
			return needExp;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFreeReward() {
			return freeReward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getPremiumReward() {
			return premiumReward;
		}
		public com.xiugou.x1.design.struct.CostThing getBuyLevelCost() {
			return buyLevelCost;
		}
	}

}