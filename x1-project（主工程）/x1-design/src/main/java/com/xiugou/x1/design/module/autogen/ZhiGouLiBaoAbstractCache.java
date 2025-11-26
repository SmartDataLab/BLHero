package com.xiugou.x1.design.module.autogen;


public abstract class ZhiGouLiBaoAbstractCache<T extends ZhiGouLiBaoAbstractCache.ZhiGouLiBaoCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1009直购礼包_ZhiGouLiBao";
	}
	protected java.util.HashMap<Integer, T> chargeProductIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引chargeProductIdIndex
		java.util.HashMap<Integer, T> chargeProductIdIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			chargeProductIdIndex.put(data.getChargeProductId(), data);
		}
		this.chargeProductIdIndex = chargeProductIdIndex;
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

	public final T getInChargeProductIdIndex(int chargeProductId) {
		T t = chargeProductIdIndex.get(chargeProductId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhiGouLiBaoCache.getInChargeProductIdIndex", chargeProductId);
		}
		return t;
	}

	public final T findInChargeProductIdIndex(int chargeProductId) {
		T t = chargeProductIdIndex.get(chargeProductId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhiGouLiBaoCache.getInActivityIdCollector", activityId);
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

	public static class ZhiGouLiBaoCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 礼包ID
		 */
		protected int id;
		/**
		 * 活动ID
		 */
		protected int activityId;
		/**
		 * 充值表ID
		 */
		protected int chargeProductId;
		/**
		 * 个性化奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> diffReward;
		/**
		 * 限购类型
		 */
		protected int limitType;
		/**
		 * 限购次数
		 */
		protected int limitNum;
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
		public int getChargeProductId() {
			return chargeProductId;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getDiffReward() {
			return diffReward;
		}
		public int getLimitType() {
			return limitType;
		}
		public int getLimitNum() {
			return limitNum;
		}
	}

}