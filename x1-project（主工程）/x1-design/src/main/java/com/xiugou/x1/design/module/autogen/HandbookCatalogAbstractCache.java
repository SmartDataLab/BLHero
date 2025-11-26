package com.xiugou.x1.design.module.autogen;


public abstract class HandbookCatalogAbstractCache<T extends HandbookCatalogAbstractCache.HandbookCatalogCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T图鉴层级表_HandbookCatalog";
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
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookCatalogCache.getInTypeCollector", type);
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

	public static class HandbookCatalogCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 图鉴类型
		 */
		protected int type;
		/**
		 * 分组
		 */
		protected int group;
		/**
		 * 组名
		 */
		protected String groupName;
		/**
		 * 子类
		 */
		protected int subtype;
		/**
		 * 品质
		 */
		protected int quality;
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
		public int getGroup() {
			return group;
		}
		public String getGroupName() {
			return groupName;
		}
		public int getSubtype() {
			return subtype;
		}
		public int getQuality() {
			return quality;
		}
	}

}