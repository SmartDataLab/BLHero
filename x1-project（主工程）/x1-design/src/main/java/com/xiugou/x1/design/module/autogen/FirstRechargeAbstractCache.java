package com.xiugou.x1.design.module.autogen;


public abstract class FirstRechargeAbstractCache<T extends FirstRechargeAbstractCache.FirstRechargeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "S首充奖励_firstRecharge";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> rechargeIdDayIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引rechargeIdDayIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> rechargeIdDayIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = rechargeIdDayIndex.get(data.getRechargeId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				rechargeIdDayIndex.put(data.getRechargeId(), layer1Map);
			}
			layer1Map.put(data.getDay(), data);
		}
		this.rechargeIdDayIndex = rechargeIdDayIndex;
	}

	public final T getInRechargeIdDayIndex(int rechargeId, int day) {
		java.util.HashMap<Integer, T> layer1Map = rechargeIdDayIndex.get(rechargeId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("FirstRechargeCache.getInRechargeIdDayIndex", rechargeId, day);
		}
		T t = layer1Map.get(day);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("FirstRechargeCache.getInRechargeIdDayIndex", rechargeId, day);
		}
		return t;
	}

	public final T findInRechargeIdDayIndex(int rechargeId, int day) {
		java.util.HashMap<Integer, T> layer1Map = rechargeIdDayIndex.get(rechargeId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(day);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class FirstRechargeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 充值ID
		 */
		protected int rechargeId;
		/**
		 * 奖励天数
		 */
		protected int day;
		/**
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> reward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getRechargeId() {
			return rechargeId;
		}
		public int getDay() {
			return day;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getReward() {
			return reward;
		}
	}

}