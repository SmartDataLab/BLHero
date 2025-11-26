package com.xiugou.x1.design.module.autogen;


public abstract class MainlineTreasureBoxAbstractCache<T extends MainlineTreasureBoxAbstractCache.MainlineTreasureBoxCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C场景宝箱配置表_MainlineTreasureBox";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeTypeArgIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeTypeArgIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeTypeArgIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = typeTypeArgIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				typeTypeArgIndex.put(data.getType(), layer1Map);
			}
			layer1Map.put(data.getTypeArg(), data);
		}
		this.typeTypeArgIndex = typeTypeArgIndex;
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

	public final T getInTypeTypeArgIndex(int type, int typeArg) {
		java.util.HashMap<Integer, T> layer1Map = typeTypeArgIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MainlineTreasureBoxCache.getInTypeTypeArgIndex", type, typeArg);
		}
		T t = layer1Map.get(typeArg);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MainlineTreasureBoxCache.getInTypeTypeArgIndex", type, typeArg);
		}
		return t;
	}

	public final T findInTypeTypeArgIndex(int type, int typeArg) {
		java.util.HashMap<Integer, T> layer1Map = typeTypeArgIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(typeArg);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MainlineTreasureBoxCache.getInTypeCollector", type);
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

	public static class MainlineTreasureBoxCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int idx;
		/**
		 * 宝箱类型
		 */
		protected int type;
		/**
		 * 类型参数
		 */
		protected int typeArg;
		/**
		 * 宝箱奖励
		 */
		protected java.util.List<Integer> reward;
		@Override
		public int id() {
			return idx;
		}
		public int getIdx() {
			return idx;
		}
		public int getType() {
			return type;
		}
		public int getTypeArg() {
			return typeArg;
		}
		public java.util.List<Integer> getReward() {
			return reward;
		}
	}

}