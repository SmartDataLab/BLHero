package com.xiugou.x1.design.module.autogen;


public abstract class DayRechargeAbstractCache<T extends DayRechargeAbstractCache.DayRechargeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M每日充值_DayRecharge";
	}
	protected java.util.HashMap<Integer, T> productIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> roundCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引productIdIndex
		java.util.HashMap<Integer, T> productIdIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			productIdIndex.put(data.getProductId(), data);
		}
		this.productIdIndex = productIdIndex;
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

	public final T getInProductIdIndex(int productId) {
		T t = productIdIndex.get(productId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DayRechargeCache.getInProductIdIndex", productId);
		}
		return t;
	}

	public final T findInProductIdIndex(int productId) {
		T t = productIdIndex.get(productId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInRoundCollector(int round) {
		java.util.ArrayList<T> ts = roundCollector.get(round);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DayRechargeCache.getInRoundCollector", round);
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

	public static class DayRechargeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 轮数
		 */
		protected int round;
		/**
		 * 充值商品ID
		 */
		protected int productId;
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
		public int getProductId() {
			return productId;
		}
	}

}