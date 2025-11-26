package com.xiugou.x1.design.module.autogen;


public abstract class HomeProducePoolAbstractCache<T extends HomeProducePoolAbstractCache.HomeProducePoolCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J家园产出池等级表_HomeProducePool";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> buildingIdLevelIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引buildingIdLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> buildingIdLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = buildingIdLevelIndex.get(data.getBuildingId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				buildingIdLevelIndex.put(data.getBuildingId(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.buildingIdLevelIndex = buildingIdLevelIndex;
	}

	public final T getInBuildingIdLevelIndex(int buildingId, int level) {
		java.util.HashMap<Integer, T> layer1Map = buildingIdLevelIndex.get(buildingId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HomeProducePoolCache.getInBuildingIdLevelIndex", buildingId, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HomeProducePoolCache.getInBuildingIdLevelIndex", buildingId, level);
		}
		return t;
	}

	public final T findInBuildingIdLevelIndex(int buildingId, int level) {
		java.util.HashMap<Integer, T> layer1Map = buildingIdLevelIndex.get(buildingId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class HomeProducePoolCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 建筑物ID
		 */
		protected int buildingId;
		/**
		 * 资源池等级
		 */
		protected int level;
		/**
		 * 升级消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing upCost;
		/**
		 * 最大容量
		 */
		protected long maxStore;
		/**
		 * 产出间隔秒
		 */
		protected int produceGap;
		/**
		 * 产出的道具ID
		 */
		protected int produceType;
		/**
		 * 每个时间间隔产出的数量
		 */
		protected long produce;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getBuildingId() {
			return buildingId;
		}
		public int getLevel() {
			return level;
		}
		public com.xiugou.x1.design.struct.CostThing getUpCost() {
			return upCost;
		}
		public long getMaxStore() {
			return maxStore;
		}
		public int getProduceGap() {
			return produceGap;
		}
		public int getProduceType() {
			return produceType;
		}
		public long getProduce() {
			return produce;
		}
	}

}