package com.xiugou.x1.design.module.autogen;


public abstract class PurgatoryAttrAbstractCache<T extends PurgatoryAttrAbstractCache.PurgatoryAttrCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L炼狱属性库_PurgatoryAttr";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> stashIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引stashIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> stashIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = stashIdCollector.get(data.getStashId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				stashIdCollector.put(data.getStashId(), collector);
			}
			collector.add(data);
		}
		this.stashIdCollector = stashIdCollector;
	}



	public final java.util.ArrayList<T> getInStashIdCollector(int stashId) {
		java.util.ArrayList<T> ts = stashIdCollector.get(stashId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("PurgatoryAttrCache.getInStashIdCollector", stashId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInStashIdCollector(int stashId) {
		java.util.ArrayList<T> ts = stashIdCollector.get(stashId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class PurgatoryAttrCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 属性库ID
		 */
		protected int stashId;
		/**
		 * 属性
		 */
		protected com.xiugou.x1.battle.config.Attr attr;
		/**
		 * 权重
		 */
		protected int weight;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getStashId() {
			return stashId;
		}
		public com.xiugou.x1.battle.config.Attr getAttr() {
			return attr;
		}
		public int getWeight() {
			return weight;
		}
	}

}