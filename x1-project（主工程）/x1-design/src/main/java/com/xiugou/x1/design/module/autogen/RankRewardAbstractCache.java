package com.xiugou.x1.design.module.autogen;


public abstract class RankRewardAbstractCache<T extends RankRewardAbstractCache.RankRewardCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P排行榜奖励表_RankReward";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeRewardIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeRewardIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeRewardIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = typeRewardIdIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				typeRewardIdIndex.put(data.getType(), layer1Map);
			}
			layer1Map.put(data.getRewardId(), data);
		}
		this.typeRewardIdIndex = typeRewardIdIndex;
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

	public final T getInTypeRewardIdIndex(int type, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = typeRewardIdIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("RankRewardCache.getInTypeRewardIdIndex", type, rewardId);
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("RankRewardCache.getInTypeRewardIdIndex", type, rewardId);
		}
		return t;
	}

	public final T findInTypeRewardIdIndex(int type, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = typeRewardIdIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("RankRewardCache.getInTypeCollector", type);
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

	public static class RankRewardCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 类型
		 */
		protected int type;
		/**
		 * 奖励ID
		 */
		protected int rewardId;
		/**
		 * 达到条件
		 */
		protected int condition;
		/**
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> reward;
		/**
		 * 条件描述
		 */
		protected String tex;
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
		public int getRewardId() {
			return rewardId;
		}
		public int getCondition() {
			return condition;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getReward() {
			return reward;
		}
		public String getTex() {
			return tex;
		}
	}

}