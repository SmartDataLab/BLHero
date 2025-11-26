package com.xiugou.x1.design.module.autogen;


public abstract class SceneFogAbstractCache<T extends SceneFogAbstractCache.SceneFogCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z主线野外迷雾表_SceneFog";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> mainlineIdFogIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引mainlineIdFogIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> mainlineIdFogIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = mainlineIdFogIdIndex.get(data.getMainlineId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				mainlineIdFogIdIndex.put(data.getMainlineId(), layer1Map);
			}
			layer1Map.put(data.getFogId(), data);
		}
		this.mainlineIdFogIdIndex = mainlineIdFogIdIndex;
	}

	public final T getInMainlineIdFogIdIndex(int mainlineId, int fogId) {
		java.util.HashMap<Integer, T> layer1Map = mainlineIdFogIdIndex.get(mainlineId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneFogCache.getInMainlineIdFogIdIndex", mainlineId, fogId);
		}
		T t = layer1Map.get(fogId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneFogCache.getInMainlineIdFogIdIndex", mainlineId, fogId);
		}
		return t;
	}

	public final T findInMainlineIdFogIdIndex(int mainlineId, int fogId) {
		java.util.HashMap<Integer, T> layer1Map = mainlineIdFogIdIndex.get(mainlineId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(fogId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SceneFogCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 场景ID
		 */
		protected int mainlineId;
		/**
		 * 迷雾点ID
		 */
		protected int fogId;
		/**
		 * 开启消耗
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> openCosts;
		/**
		 * 开启奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing openRewards;
		/**
		 * 所属区域ID
		 */
		protected int belongZoneId;
		/**
		 * 开启等级
		 */
		protected int openLevel;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getMainlineId() {
			return mainlineId;
		}
		public int getFogId() {
			return fogId;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getOpenCosts() {
			return openCosts;
		}
		public com.xiugou.x1.design.struct.RewardThing getOpenRewards() {
			return openRewards;
		}
		public int getBelongZoneId() {
			return belongZoneId;
		}
		public int getOpenLevel() {
			return openLevel;
		}
	}

}