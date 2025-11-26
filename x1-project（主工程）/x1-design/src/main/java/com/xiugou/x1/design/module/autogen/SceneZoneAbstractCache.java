package com.xiugou.x1.design.module.autogen;


public abstract class SceneZoneAbstractCache<T extends SceneZoneAbstractCache.SceneZoneCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C场景区域表_SceneZone";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdZoneIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> sceneIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引sceneIdZoneIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdZoneIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = sceneIdZoneIdIndex.get(data.getSceneId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				sceneIdZoneIdIndex.put(data.getSceneId(), layer1Map);
			}
			layer1Map.put(data.getZoneId(), data);
		}
		this.sceneIdZoneIdIndex = sceneIdZoneIdIndex;
		//构建索引sceneIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> sceneIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = sceneIdCollector.get(data.getSceneId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				sceneIdCollector.put(data.getSceneId(), collector);
			}
			collector.add(data);
		}
		this.sceneIdCollector = sceneIdCollector;
	}

	public final T getInSceneIdZoneIdIndex(int sceneId, int zoneId) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdZoneIdIndex.get(sceneId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneZoneCache.getInSceneIdZoneIdIndex", sceneId, zoneId);
		}
		T t = layer1Map.get(zoneId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneZoneCache.getInSceneIdZoneIdIndex", sceneId, zoneId);
		}
		return t;
	}

	public final T findInSceneIdZoneIdIndex(int sceneId, int zoneId) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdZoneIdIndex.get(sceneId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(zoneId);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInSceneIdCollector(int sceneId) {
		java.util.ArrayList<T> ts = sceneIdCollector.get(sceneId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneZoneCache.getInSceneIdCollector", sceneId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInSceneIdCollector(int sceneId) {
		java.util.ArrayList<T> ts = sceneIdCollector.get(sceneId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class SceneZoneCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 场景ID
		 */
		protected int sceneId;
		/**
		 * 场景类型
		 */
		protected int sceneType;
		/**
		 * 区域ID
		 */
		protected int zoneId;
		/**
		 * 备注
		 */
		protected String comment;
		/**
		 * 地区刷挂数据文件
		 */
		protected String mapData;
		/**
		 * 是否安全区
		 */
		protected int safeZone;
		/**
		 * 背景音效
		 */
		protected String bgMusic;
		/**
		 * 地图资源
		 */
		protected String mapResource;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSceneId() {
			return sceneId;
		}
		public int getSceneType() {
			return sceneType;
		}
		public int getZoneId() {
			return zoneId;
		}
		public String getComment() {
			return comment;
		}
		public String getMapData() {
			return mapData;
		}
		public int getSafeZone() {
			return safeZone;
		}
		public String getBgMusic() {
			return bgMusic;
		}
		public String getMapResource() {
			return mapResource;
		}
	}

}