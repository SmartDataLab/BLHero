package com.xiugou.x1.design.module.autogen;


public abstract class EvilSuitAbstractCache<T extends EvilSuitAbstractCache.EvilSuitCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y妖傀套装_EvilSuit";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> suitIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引suitIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> suitIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = suitIdCollector.get(data.getSuitId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				suitIdCollector.put(data.getSuitId(), collector);
			}
			collector.add(data);
		}
		this.suitIdCollector = suitIdCollector;
	}



	public final java.util.ArrayList<T> getInSuitIdCollector(int suitId) {
		java.util.ArrayList<T> ts = suitIdCollector.get(suitId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EvilSuitCache.getInSuitIdCollector", suitId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInSuitIdCollector(int suitId) {
		java.util.ArrayList<T> ts = suitIdCollector.get(suitId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class EvilSuitCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 套装Id
		 */
		protected int suitId;
		/**
		 * 套装数量
		 */
		protected int suitNum;
		/**
		 * 装备套装属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attrs;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSuitId() {
			return suitId;
		}
		public int getSuitNum() {
			return suitNum;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttrs() {
			return attrs;
		}
	}

}