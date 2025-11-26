package com.xiugou.x1.design.module.autogen;


public abstract class TowerAbstractCache<T extends TowerAbstractCache.TowerCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T通天塔_Tower";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeLayerIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeLayerIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> typeLayerIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = typeLayerIndex.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				typeLayerIndex.put(data.getType(), layer1Map);
			}
			layer1Map.put(data.getLayer(), data);
		}
		this.typeLayerIndex = typeLayerIndex;
	}

	public final T getInTypeLayerIndex(int type, int layer) {
		java.util.HashMap<Integer, T> layer1Map = typeLayerIndex.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TowerCache.getInTypeLayerIndex", type, layer);
		}
		T t = layer1Map.get(layer);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TowerCache.getInTypeLayerIndex", type, layer);
		}
		return t;
	}

	public final T findInTypeLayerIndex(int type, int layer) {
		java.util.HashMap<Integer, T> layer1Map = typeLayerIndex.get(type);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(layer);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class TowerCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 塔类型
		 */
		protected int type;
		/**
		 * 层数
		 */
		protected int layer;
		/**
		 * 通关奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		/**
		 * 怪物
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Monster> monster;
		/**
		 * 刷怪点类型（0固定 1随机）
		 */
		protected int refreshType;
		/**
		 * 爬塔每日奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> dailyRewards;
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
		public int getLayer() {
			return layer;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.Monster> getMonster() {
			return monster;
		}
		public int getRefreshType() {
			return refreshType;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getDailyRewards() {
			return dailyRewards;
		}
	}

}