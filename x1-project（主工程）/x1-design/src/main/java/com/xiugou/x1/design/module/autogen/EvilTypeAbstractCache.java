package com.xiugou.x1.design.module.autogen;


public abstract class EvilTypeAbstractCache<T extends EvilTypeAbstractCache.EvilTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y妖傀类型_EvilType";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> seriesCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引seriesCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> seriesCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = seriesCollector.get(data.getSeries());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				seriesCollector.put(data.getSeries(), collector);
			}
			collector.add(data);
		}
		this.seriesCollector = seriesCollector;
	}



	public final java.util.ArrayList<T> getInSeriesCollector(int series) {
		java.util.ArrayList<T> ts = seriesCollector.get(series);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EvilTypeCache.getInSeriesCollector", series);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInSeriesCollector(int series) {
		java.util.ArrayList<T> ts = seriesCollector.get(series);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class EvilTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 标识
		 */
		protected int identity;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 品质
		 */
		protected int quality;
		/**
		 * 系列
		 */
		protected int series;
		@Override
		public int id() {
			return identity;
		}
		public int getIdentity() {
			return identity;
		}
		public String getName() {
			return name;
		}
		public int getQuality() {
			return quality;
		}
		public int getSeries() {
			return series;
		}
	}

}