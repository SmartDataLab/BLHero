package com.xiugou.x1.design.module.autogen;


public abstract class MeiRiChongZhiAbstractCache<T extends MeiRiChongZhiAbstractCache.MeiRiChongZhiCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1011每日充值_MeiRiChongZhi";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> activityIdRewardIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引activityIdRewardIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> activityIdRewardIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = activityIdRewardIdIndex.get(data.getActivityId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				activityIdRewardIdIndex.put(data.getActivityId(), layer1Map);
			}
			layer1Map.put(data.getRewardId(), data);
		}
		this.activityIdRewardIdIndex = activityIdRewardIdIndex;
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

	public final T getInActivityIdRewardIdIndex(int activityId, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = activityIdRewardIdIndex.get(activityId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MeiRiChongZhiCache.getInActivityIdRewardIdIndex", activityId, rewardId);
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MeiRiChongZhiCache.getInActivityIdRewardIdIndex", activityId, rewardId);
		}
		return t;
	}

	public final T findInActivityIdRewardIdIndex(int activityId, int rewardId) {
		java.util.HashMap<Integer, T> layer1Map = activityIdRewardIdIndex.get(activityId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(rewardId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MeiRiChongZhiCache.getInActivityIdCollector", activityId);
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

	public static class MeiRiChongZhiCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 活动ID
		 */
		protected int activityId;
		/**
		 * 奖励ID
		 */
		protected int rewardId;
		/**
		 * 充值数量
		 */
		protected long targetNum;
		/**
		 * 自选奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> selectRewards;
		/**
		 * 固定奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		/**
		 * 描述
		 */
		protected String desc;
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
		public int getRewardId() {
			return rewardId;
		}
		public long getTargetNum() {
			return targetNum;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getSelectRewards() {
			return selectRewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
		public String getDesc() {
			return desc;
		}
	}

}