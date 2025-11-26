package com.xiugou.x1.design.module.autogen;


public abstract class RechargeProductAbstractCache<T extends RechargeProductAbstractCache.RechargeProductCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C充值商品表_RechargeProduct";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> productTypeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引productTypeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> productTypeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = productTypeCollector.get(data.getProductType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				productTypeCollector.put(data.getProductType(), collector);
			}
			collector.add(data);
		}
		this.productTypeCollector = productTypeCollector;
	}



	public final java.util.ArrayList<T> getInProductTypeCollector(int productType) {
		java.util.ArrayList<T> ts = productTypeCollector.get(productType);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("RechargeProductCache.getInProductTypeCollector", productType);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInProductTypeCollector(int productType) {
		java.util.ArrayList<T> ts = productTypeCollector.get(productType);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class RechargeProductCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 充值商品ID
		 */
		protected int id;
		/**
		 * 商品名称
		 */
		protected String name;
		/**
		 * 商品逻辑类型
		 */
		protected int productType;
		/**
		 * 商品充值参数
		 */
		protected String productParam;
		/**
		 * 金额单位分
		 */
		protected long money;
		/**
		 * 商品编码
		 */
		protected String productCode;
		/**
		 * 充值奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		/**
		 * 首充额外奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> firstRewards;
		/**
		 * 贵族经验奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing noble;
		/**
		 * 商品描述
		 */
		protected String describe;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public int getProductType() {
			return productType;
		}
		public String getProductParam() {
			return productParam;
		}
		public long getMoney() {
			return money;
		}
		public String getProductCode() {
			return productCode;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFirstRewards() {
			return firstRewards;
		}
		public com.xiugou.x1.design.struct.RewardThing getNoble() {
			return noble;
		}
		public String getDescribe() {
			return describe;
		}
	}

}