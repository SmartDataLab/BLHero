package com.xiugou.x1.design.module.autogen;


public abstract class ChengZhangJiJinAbstractCache<T extends ChengZhangJiJinAbstractCache.ChengZhangJiJinCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1006成长基金_ChengZhangJiJin";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> roundRewardIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> roundCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引roundRewardIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> roundRewardIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = roundRewardIdIndex.get(data.getRound());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				roundRewardIdIndex.put(data.getRound(), layer1Map);
			}
			layer1Map.put(data.getRewardId(), data);
		}
		this.roundRewardIdIndex = roundRewardIdIndex;
		//构建索引roundCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> roundCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = roundCollector.get(data.getRound());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				roundCollector.put(data.getRound(), collector);
			}
			collector.add(data);
		}
		this.roundCollector = roundCollector;
	}

	public final T getInRoundRewardIdIndex(int round, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = roundRewardIdIndex.get(round);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ChengZhangJiJinCache.getInRoundRewardIdIndex", round, rewardId);
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ChengZhangJiJinCache.getInRoundRewardIdIndex", round, rewardId);
		}
		return t;
	}

	public final T findInRoundRewardIdIndex(int round, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = roundRewardIdIndex.get(round);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInRoundCollector(int round) {
		java.util.ArrayList<T> ts = roundCollector.get(round);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ChengZhangJiJinCache.getInRoundCollector", round);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInRoundCollector(int round) {
		java.util.ArrayList<T> ts = roundCollector.get(round);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ChengZhangJiJinCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 奖励期数
		 */
		protected int round;
		/**
		 * 奖励ID
		 */
		protected int rewardId;
		/**
		 * 等级要求
		 */
		protected int level;
		/**
		 * 奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getRound() {
			return round;
		}
		public int getRewardId() {
			return rewardId;
		}
		public int getLevel() {
			return level;
		}
		public com.xiugou.x1.design.struct.RewardThing getReward() {
			return reward;
		}
	}

}