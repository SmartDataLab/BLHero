package com.xiugou.x1.design.module.autogen;


public abstract class EvilCatalogAbstractCache<T extends EvilCatalogAbstractCache.EvilCatalogCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y妖录_EvilCatalog";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> identityLevelIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> identityCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引identityLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> identityLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = identityLevelIndex.get(data.getIdentity());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				identityLevelIndex.put(data.getIdentity(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.identityLevelIndex = identityLevelIndex;
		//构建索引identityCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> identityCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = identityCollector.get(data.getIdentity());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				identityCollector.put(data.getIdentity(), collector);
			}
			collector.add(data);
		}
		this.identityCollector = identityCollector;
	}

	public final T getInIdentityLevelIndex(int identity, int level) {
		java.util.HashMap<Integer, T> layer1Map = identityLevelIndex.get(identity);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EvilCatalogCache.getInIdentityLevelIndex", identity, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EvilCatalogCache.getInIdentityLevelIndex", identity, level);
		}
		return t;
	}

	public final T findInIdentityLevelIndex(int identity, int level) {
		java.util.HashMap<Integer, T> layer1Map = identityLevelIndex.get(identity);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInIdentityCollector(int identity) {
		java.util.ArrayList<T> ts = identityCollector.get(identity);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EvilCatalogCache.getInIdentityCollector", identity);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInIdentityCollector(int identity) {
		java.util.ArrayList<T> ts = identityCollector.get(identity);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class EvilCatalogCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 标识
		 */
		protected int identity;
		/**
		 * 阶级
		 */
		protected int level;
		/**
		 * 消耗本体数量
		 */
		protected int costBody;
		/**
		 * 属性加成
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attr;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getIdentity() {
			return identity;
		}
		public int getLevel() {
			return level;
		}
		public int getCostBody() {
			return costBody;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttr() {
			return attr;
		}
	}

}