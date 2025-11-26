package com.xiugou.x1.design.module.autogen;


public abstract class TreasureHouseAbstractCache<T extends TreasureHouseAbstractCache.TreasureHouseCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z珍宝阁_TreasureHouse";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> typePeriodRechargeIdIndex;
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> typePeriodRewardIdIndex;

	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> typePeriodCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typePeriodRechargeIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> typePeriodRechargeIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRechargeIdIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				typePeriodRechargeIdIndex.put(data.getType(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getPeriod());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getPeriod(), layer2Map);
			}
			layer2Map.put(data.getRechargeId(), data);
		}
		this.typePeriodRechargeIdIndex = typePeriodRechargeIdIndex;
		//构建索引typePeriodRewardIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> typePeriodRewardIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRewardIdIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				typePeriodRewardIdIndex.put(data.getType(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getPeriod());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getPeriod(), layer2Map);
			}
			layer2Map.put(data.getRewardId(), data);
		}
		this.typePeriodRewardIdIndex = typePeriodRewardIdIndex;
		//构建索引typePeriodCollector
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> typePeriodCollector = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typePeriodCollector.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
				typePeriodCollector.put(data.getType(), layer1Map);
			}
			java.util.ArrayList<T> collector = layer1Map.get(data.getPeriod());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				layer1Map.put(data.getPeriod(), collector);
			}
			collector.add(data);
		}
		this.typePeriodCollector = typePeriodCollector;
	}

	public final T getInTypePeriodRechargeIdIndex(int type, int period, int rechargeId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRechargeIdIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(period);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		T t = layer2Map.get(rechargeId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRechargeIdIndex", type, period, rechargeId);
		}
		return t;
	}
	public final T getInTypePeriodRewardIdIndex(int type, int period, int rewardId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRewardIdIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(period);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		T t = layer2Map.get(rewardId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodRewardIdIndex", type, period, rewardId);
		}
		return t;
	}

	public final T findInTypePeriodRechargeIdIndex(int type, int period, int rechargeId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRechargeIdIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(period);
		if(layer2Map == null) {
			return null;
		}
		T t = layer2Map.get(rechargeId);
		if(t == null) {
			return null;
		}
		return t;
	}
	public final T findInTypePeriodRewardIdIndex(int type, int period, int rewardId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = typePeriodRewardIdIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(period);
		if(layer2Map == null) {
			return null;
		}
		T t = layer2Map.get(rewardId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInTypePeriodCollector(int type, int period) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typePeriodCollector.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodCollector", type, period);
		}
		java.util.ArrayList<T> ts = layer1Map.get(period);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TreasureHouseCache.getInTypePeriodCollector", type, period);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTypePeriodCollector(int type, int period) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typePeriodCollector.get(type);
		if(layer1Map == null) {
			return null;
		}
		java.util.ArrayList<T> ts = layer1Map.get(period);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class TreasureHouseCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 类型
		 */
		protected int type;
		/**
		 * 期数
		 */
		protected int period;
		/**
		 * 充值商品表ID
		 */
		protected int rechargeId;
		/**
		 * 是否为免费商品
		 */
		protected int rewardId;
		/**
		 * 限制条件
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> condition;
		/**
		 * 限购类型
		 */
		protected int limitType;
		/**
		 * 限购数量
		 */
		protected int limitNum;
		/**
		 * 商品内容
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> reward;
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
		public int getPeriod() {
			return period;
		}
		public int getRechargeId() {
			return rechargeId;
		}
		public int getRewardId() {
			return rewardId;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getCondition() {
			return condition;
		}
		public int getLimitType() {
			return limitType;
		}
		public int getLimitNum() {
			return limitNum;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getReward() {
			return reward;
		}
	}

}