package com.xiugou.x1.design.module.autogen;


public abstract class DungeonMonsterAbstractCache<T extends DungeonMonsterAbstractCache.DungeonMonsterCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M秘境副本刷怪表_DungeonMonster";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> sceneIdZoneIdRefreshIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引sceneIdZoneIdRefreshIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> sceneIdZoneIdRefreshIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = sceneIdZoneIdRefreshIdIndex.get(data.getSceneId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				sceneIdZoneIdRefreshIdIndex.put(data.getSceneId(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getZoneId());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getZoneId(), layer2Map);
			}
			layer2Map.put(data.getRefreshId(), data);
		}
		this.sceneIdZoneIdRefreshIdIndex = sceneIdZoneIdRefreshIdIndex;
	}

	public final T getInSceneIdZoneIdRefreshIdIndex(int sceneId, int zoneId, int refreshId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = sceneIdZoneIdRefreshIdIndex.get(sceneId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(zoneId);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		T t = layer2Map.get(refreshId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DungeonMonsterCache.getInSceneIdZoneIdRefreshIdIndex", sceneId, zoneId, refreshId);
		}
		return t;
	}

	public final T findInSceneIdZoneIdRefreshIdIndex(int sceneId, int zoneId, int refreshId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = sceneIdZoneIdRefreshIdIndex.get(sceneId);
		if(layer1Map == null) {
			return null;
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(zoneId);
		if(layer2Map == null) {
			return null;
		}
		T t = layer2Map.get(refreshId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class DungeonMonsterCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 副本ID
		 */
		protected int sceneId;
		/**
		 * 场景区域ID
		 */
		protected int zoneId;
		/**
		 * 怪物刷新点
		 */
		protected int refreshId;
		/**
		 * 怪物ID
		 */
		protected int monsterId;
		/**
		 * 刷怪点类型（0固定 1随机）
		 */
		protected int refreshType;
		/**
		 * 刷怪数量
		 */
		protected int num;
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
		public int getZoneId() {
			return zoneId;
		}
		public int getRefreshId() {
			return refreshId;
		}
		public int getMonsterId() {
			return monsterId;
		}
		public int getRefreshType() {
			return refreshType;
		}
		public int getNum() {
			return num;
		}
	}

}