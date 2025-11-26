package com.xiugou.x1.design.module.autogen;


public abstract class XianShiLiBaoAbstractCache<T extends XianShiLiBaoAbstractCache.XianShiLiBaoCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1005限时礼包_XianShiLiBao";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
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



	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("XianShiLiBaoCache.getInTypeCollector", type);
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

	public static class XianShiLiBaoCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 礼包ID
		 */
		protected int id;
		/**
		 * 礼包类型
		 */
		protected int type;
		/**
		 * 充值表ID
		 */
		protected int chargeProductId;
		/**
		 * Vip等级
		 */
		protected int vipLevel;
		/**
		 * 触发条件
		 */
		protected java.util.List<Integer> condition;
		/**
		 * 个性化奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> diffReward;
		/**
		 * 有效期/分钟
		 */
		protected int countdown;
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
		public int getChargeProductId() {
			return chargeProductId;
		}
		public int getVipLevel() {
			return vipLevel;
		}
		public java.util.List<Integer> getCondition() {
			return condition;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getDiffReward() {
			return diffReward;
		}
		public int getCountdown() {
			return countdown;
		}
	}

}