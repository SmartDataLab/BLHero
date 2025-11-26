package com.xiugou.x1.design.module.autogen;


public abstract class ExchangeShopAbstractCache<T extends ExchangeShopAbstractCache.ExchangeShopCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "D兑换商店_ExchangeShop";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> shopIdRoundProductIdIndex;

	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> shopIdRoundCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引shopIdRoundProductIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> shopIdRoundProductIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = shopIdRoundProductIdIndex.get(data.getShopId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				shopIdRoundProductIdIndex.put(data.getShopId(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getRound());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getRound(), layer2Map);
			}
			layer2Map.put(data.getProductId(), data);
		}
		this.shopIdRoundProductIdIndex = shopIdRoundProductIdIndex;
		//构建索引shopIdRoundCollector
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> shopIdRoundCollector = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = shopIdRoundCollector.get(data.getShopId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
				shopIdRoundCollector.put(data.getShopId(), layer1Map);
			}
			java.util.ArrayList<T> collector = layer1Map.get(data.getRound());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				layer1Map.put(data.getRound(), collector);
			}
			collector.add(data);
		}
		this.shopIdRoundCollector = shopIdRoundCollector;
	}

	public final T getInShopIdRoundProductIdIndex(int shopId, int round, int productId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = shopIdRoundProductIdIndex.get(shopId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(round);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		T t = layer2Map.get(productId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ExchangeShopCache.getInShopIdRoundProductIdIndex", shopId, round, productId);
		}
		return t;
	}

	public final T findInShopIdRoundProductIdIndex(int shopId, int round, int productId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = shopIdRoundProductIdIndex.get(shopId);
		if(layer1Map == null) {
			return null;
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(round);
		if(layer2Map == null) {
			return null;
		}
		T t = layer2Map.get(productId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInShopIdRoundCollector(int shopId, int round) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = shopIdRoundCollector.get(shopId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ExchangeShopCache.getInShopIdRoundCollector", shopId, round);
		}
		java.util.ArrayList<T> ts = layer1Map.get(round);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ExchangeShopCache.getInShopIdRoundCollector", shopId, round);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInShopIdRoundCollector(int shopId, int round) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = shopIdRoundCollector.get(shopId);
		if(layer1Map == null) {
			return null;
		}
		java.util.ArrayList<T> ts = layer1Map.get(round);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ExchangeShopCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 商店ID
		 */
		protected int shopId;
		/**
		 * 期数
		 */
		protected int round;
		/**
		 * 商品ID
		 */
		protected int productId;
		/**
		 * 商品内容
		 */
		protected com.xiugou.x1.design.struct.RewardThing product;
		/**
		 * 开启条件
		 */
		protected com.xiugou.x1.design.struct.Keyv condition;
		/**
		 * 限购类型
		 */
		protected int limitType;
		/**
		 * 最大兑换次数
		 */
		protected int limit;
		/**
		 * 兑换消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing cost;
		/**
		 * 免费次数
		 */
		protected int freeTimes;
		/**
		 * 广告次数
		 */
		protected int advertising;
		/**
		 * 开服x天消失
		 */
		protected int closeDay;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getShopId() {
			return shopId;
		}
		public int getRound() {
			return round;
		}
		public int getProductId() {
			return productId;
		}
		public com.xiugou.x1.design.struct.RewardThing getProduct() {
			return product;
		}
		public com.xiugou.x1.design.struct.Keyv getCondition() {
			return condition;
		}
		public int getLimitType() {
			return limitType;
		}
		public int getLimit() {
			return limit;
		}
		public com.xiugou.x1.design.struct.CostThing getCost() {
			return cost;
		}
		public int getFreeTimes() {
			return freeTimes;
		}
		public int getAdvertising() {
			return advertising;
		}
		public int getCloseDay() {
			return closeDay;
		}
	}

}