package com.xiugou.x1.design.module.autogen;


public abstract class DailyWeeklyGiftBoxAbstractCache<T extends DailyWeeklyGiftBoxAbstractCache.DailyWeeklyGiftBoxCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "R日常周常活跃宝箱_DailyWeeklyGiftBox";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeRewardNumIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeRewardNumIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeRewardNumIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = typeRewardNumIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				typeRewardNumIndex.put(data.getType(), layer1Map);
			}
			layer1Map.put(data.getRewardNum(), data);
		}
		this.typeRewardNumIndex = typeRewardNumIndex;
		//构建索引typeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = typeCollector.get(data.getType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				typeCollector.put(data.getType(), collector);
			}
			collector.add(data);
		}
		this.typeCollector = typeCollector;
	}

	public final T getInTypeRewardNumIndex(int type, int rewardNum) {
		java.util.HashMap<Integer, T> layer1Map = typeRewardNumIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DailyWeeklyGiftBoxCache.getInTypeRewardNumIndex", type, rewardNum);
		}
		T t = layer1Map.get(rewardNum);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DailyWeeklyGiftBoxCache.getInTypeRewardNumIndex", type, rewardNum);
		}
		return t;
	}

	public final T findInTypeRewardNumIndex(int type, int rewardNum) {
		java.util.HashMap<Integer, T> layer1Map = typeRewardNumIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(rewardNum);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DailyWeeklyGiftBoxCache.getInTypeCollector", type);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class DailyWeeklyGiftBoxCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 编号
		 */
		protected int id;
		/**
		 * 任务类型
		 */
		protected int type;
		/**
		 * 奖励序号
		 */
		protected int rewardNum;
		/**
		 * 活跃度
		 */
		protected int points;
		/**
		 * 任务奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getType() {
			return type;
		}
		public int getRewardNum() {
			return rewardNum;
		}
		public int getPoints() {
			return points;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}