package com.xiugou.x1.design.module.autogen;


public abstract class HandbookIdentityAbstractCache<T extends HandbookIdentityAbstractCache.HandbookIdentityCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T图鉴列表配置表_HandbookIdentity";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> catalogCollector;
	protected java.util.HashMap<Integer, java.util.ArrayList<T>> suitCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引catalogCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> catalogCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = catalogCollector.get(data.getCatalog());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				catalogCollector.put(data.getCatalog(), collector);
			}
			collector.add(data);
		}
		this.catalogCollector = catalogCollector;
		//构建索引suitCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> suitCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = suitCollector.get(data.getSuit());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				suitCollector.put(data.getSuit(), collector);
			}
			collector.add(data);
		}
		this.suitCollector = suitCollector;
	}



	public final java.util.ArrayList<T> getInCatalogCollector(int catalog) {
		java.util.ArrayList<T> ts = catalogCollector.get(catalog);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookIdentityCache.getInCatalogCollector", catalog);
		}
		return ts;
	}
	public final java.util.ArrayList<T> getInSuitCollector(int suit) {
		java.util.ArrayList<T> ts = suitCollector.get(suit);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookIdentityCache.getInSuitCollector", suit);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInCatalogCollector(int catalog) {
		java.util.ArrayList<T> ts = catalogCollector.get(catalog);
		if(ts == null) {
			return null;
		}
		return ts;
	}
	public final java.util.ArrayList<T> findInSuitCollector(int suit) {
		java.util.ArrayList<T> ts = suitCollector.get(suit);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class HandbookIdentityCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 图鉴ID
		 */
		protected int id;
		/**
		 * 图鉴品质
		 */
		protected int quality;
		/**
		 * 升级方式
		 */
		protected int upType;
		/**
		 * 归属层级
		 */
		protected int catalog;
		/**
		 * 套装ID
		 */
		protected int suit;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getQuality() {
			return quality;
		}
		public int getUpType() {
			return upType;
		}
		public int getCatalog() {
			return catalog;
		}
		public int getSuit() {
			return suit;
		}
	}

}