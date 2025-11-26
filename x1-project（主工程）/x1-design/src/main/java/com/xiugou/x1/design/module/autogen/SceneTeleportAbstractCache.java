package com.xiugou.x1.design.module.autogen;


public abstract class SceneTeleportAbstractCache<T extends SceneTeleportAbstractCache.SceneTeleportCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z主线野外传送点表_SceneTeleport";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> mainlineIdTeleportIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引mainlineIdTeleportIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> mainlineIdTeleportIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = mainlineIdTeleportIdIndex.get(data.getMainlineId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				mainlineIdTeleportIdIndex.put(data.getMainlineId(), layer1Map);
			}
			layer1Map.put(data.getTeleportId(), data);
		}
		this.mainlineIdTeleportIdIndex = mainlineIdTeleportIdIndex;
	}

	public final T getInMainlineIdTeleportIdIndex(int mainlineId, int teleportId) {
		java.util.HashMap<Integer, T> layer1Map = mainlineIdTeleportIdIndex.get(mainlineId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneTeleportCache.getInMainlineIdTeleportIdIndex", mainlineId, teleportId);
		}
		T t = layer1Map.get(teleportId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneTeleportCache.getInMainlineIdTeleportIdIndex", mainlineId, teleportId);
		}
		return t;
	}

	public final T findInMainlineIdTeleportIdIndex(int mainlineId, int teleportId) {
		java.util.HashMap<Integer, T> layer1Map = mainlineIdTeleportIdIndex.get(mainlineId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(teleportId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SceneTeleportCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 主线场景ID
		 */
		protected int mainlineId;
		/**
		 * 传送点ID
		 */
		protected int teleportId;
		/**
		 * 开启消耗
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> openCosts;
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
		public int getTeleportId() {
			return teleportId;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getOpenCosts() {
			return openCosts;
		}
	}

}