package com.xiugou.x1.design.module.autogen;


public abstract class ShopTypeAbstractCache<T extends ShopTypeAbstractCache.ShopTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "S商店类型_ShopType";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> shopTypeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引shopTypeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> shopTypeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = shopTypeCollector.get(data.getShopType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				shopTypeCollector.put(data.getShopType(), collector);
			}
			collector.add(data);
		}
		this.shopTypeCollector = shopTypeCollector;
	}



	public final java.util.ArrayList<T> getInShopTypeCollector(int shopType) {
		java.util.ArrayList<T> ts = shopTypeCollector.get(shopType);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ShopTypeCache.getInShopTypeCollector", shopType);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInShopTypeCollector(int shopType) {
		java.util.ArrayList<T> ts = shopTypeCollector.get(shopType);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ShopTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 商店ID
		 */
		protected int shopId;
		/**
		 * 商店名称
		 */
		protected String shopName;
		/**
		 * 重置类型
		 */
		protected int resetType;
		/**
		 * 商店类型
		 */
		protected int shopType;
		@Override
		public int id() {
			return shopId;
		}
		public int getShopId() {
			return shopId;
		}
		public String getShopName() {
			return shopName;
		}
		public int getResetType() {
			return resetType;
		}
		public int getShopType() {
			return shopType;
		}
	}

}