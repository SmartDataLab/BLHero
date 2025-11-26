package com.xiugou.x1.design.module.autogen;


public abstract class NameRandomAbstractCache<T extends NameRandomAbstractCache.NameRandomCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M名字随机_NameRandom";
	}

	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> sexTypeCollector;
	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引sexTypeCollector
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> sexTypeCollector = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = sexTypeCollector.get(data.getSex());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
				sexTypeCollector.put(data.getSex(), layer1Map);
			}
			java.util.ArrayList<T> collector = layer1Map.get(data.getType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				layer1Map.put(data.getType(), collector);
			}
			collector.add(data);
		}
		this.sexTypeCollector = sexTypeCollector;
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



	public final java.util.ArrayList<T> getInSexTypeCollector(int sex, int type) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = sexTypeCollector.get(sex);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("NameRandomCache.getInSexTypeCollector", sex, type);
		}
		java.util.ArrayList<T> ts = layer1Map.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("NameRandomCache.getInSexTypeCollector", sex, type);
		}
		return ts;
	}
	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("NameRandomCache.getInTypeCollector", type);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInSexTypeCollector(int sex, int type) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = sexTypeCollector.get(sex);
		if(layer1Map == null) {
			return null;
		}
		java.util.ArrayList<T> ts = layer1Map.get(type);
		if(ts == null) {
			return null;
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

	public static class NameRandomCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 性别
		 */
		protected int sex;
		/**
		 * 类型
		 */
		protected int type;
		/**
		 * 字库
		 */
		protected String name;
		/**
		 * 英文字库
		 */
		protected String en;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSex() {
			return sex;
		}
		public int getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public String getEn() {
			return en;
		}
	}

}