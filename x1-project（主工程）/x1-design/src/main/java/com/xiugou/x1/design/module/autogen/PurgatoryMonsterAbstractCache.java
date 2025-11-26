package com.xiugou.x1.design.module.autogen;


public abstract class PurgatoryMonsterAbstractCache<T extends PurgatoryMonsterAbstractCache.PurgatoryMonsterCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L炼狱轮回怪物表_PurgatoryMonster";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> difficultyZoneIdRefreshIdIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> difficultyCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引difficultyZoneIdRefreshIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> difficultyZoneIdRefreshIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = difficultyZoneIdRefreshIdIndex.get(data.getDifficulty());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				difficultyZoneIdRefreshIdIndex.put(data.getDifficulty(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getZoneId());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getZoneId(), layer2Map);
			}
			layer2Map.put(data.getRefreshId(), data);
		}
		this.difficultyZoneIdRefreshIdIndex = difficultyZoneIdRefreshIdIndex;
		//构建索引difficultyCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> difficultyCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = difficultyCollector.get(data.getDifficulty());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				difficultyCollector.put(data.getDifficulty(), collector);
			}
			collector.add(data);
		}
		this.difficultyCollector = difficultyCollector;
	}

	public final T getInDifficultyZoneIdRefreshIdIndex(int difficulty, int zoneId, int refreshId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = difficultyZoneIdRefreshIdIndex.get(difficulty);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(zoneId);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		T t = layer2Map.get(refreshId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("PurgatoryMonsterCache.getInDifficultyZoneIdRefreshIdIndex", difficulty, zoneId, refreshId);
		}
		return t;
	}

	public final T findInDifficultyZoneIdRefreshIdIndex(int difficulty, int zoneId, int refreshId) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = difficultyZoneIdRefreshIdIndex.get(difficulty);
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

	public final java.util.ArrayList<T> getInDifficultyCollector(int difficulty) {
		java.util.ArrayList<T> ts = difficultyCollector.get(difficulty);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("PurgatoryMonsterCache.getInDifficultyCollector", difficulty);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInDifficultyCollector(int difficulty) {
		java.util.ArrayList<T> ts = difficultyCollector.get(difficulty);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class PurgatoryMonsterCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 难度
		 */
		protected int difficulty;
		/**
		 * 场景区域ID
		 */
		protected int zoneId;
		/**
		 * 怪物刷新点
		 */
		protected int refreshId;
		/**
		 * 怪物
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> monster;
		/**
		 * 刷怪点类型（0固定 1随机）
		 */
		protected int refreshType;
		/**
		 * 时间/秒
		 */
		protected int time;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getDifficulty() {
			return difficulty;
		}
		public int getZoneId() {
			return zoneId;
		}
		public int getRefreshId() {
			return refreshId;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getMonster() {
			return monster;
		}
		public int getRefreshType() {
			return refreshType;
		}
		public int getTime() {
			return time;
		}
	}

}