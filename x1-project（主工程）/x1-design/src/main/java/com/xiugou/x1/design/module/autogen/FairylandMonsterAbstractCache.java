package com.xiugou.x1.design.module.autogen;


public abstract class FairylandMonsterAbstractCache<T extends FairylandMonsterAbstractCache.FairylandMonsterCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X仙境守卫怪物表(村庄)_FairylandMonster";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> difficultyWaveIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> difficultyCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引difficultyWaveIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> difficultyWaveIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = difficultyWaveIndex.get(data.getDifficulty());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				difficultyWaveIndex.put(data.getDifficulty(), layer1Map);
			}
			layer1Map.put(data.getWave(), data);
		}
		this.difficultyWaveIndex = difficultyWaveIndex;
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

	public final T getInDifficultyWaveIndex(int difficulty, int wave) {
		java.util.HashMap<Integer, T> layer1Map = difficultyWaveIndex.get(difficulty);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("FairylandMonsterCache.getInDifficultyWaveIndex", difficulty, wave);
		}
		T t = layer1Map.get(wave);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("FairylandMonsterCache.getInDifficultyWaveIndex", difficulty, wave);
		}
		return t;
	}

	public final T findInDifficultyWaveIndex(int difficulty, int wave) {
		java.util.HashMap<Integer, T> layer1Map = difficultyWaveIndex.get(difficulty);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(wave);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInDifficultyCollector(int difficulty) {
		java.util.ArrayList<T> ts = difficultyCollector.get(difficulty);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("FairylandMonsterCache.getInDifficultyCollector", difficulty);
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

	public static class FairylandMonsterCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 难度
		 */
		protected int difficulty;
		/**
		 * 波
		 */
		protected int wave;
		/**
		 * 怪物刷新
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Monster> monsters;
		/**
		 * 刷怪点类型（0固定 1随机）
		 */
		protected int refreshType;
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
		public int getWave() {
			return wave;
		}
		public java.util.List<com.xiugou.x1.design.struct.Monster> getMonsters() {
			return monsters;
		}
		public int getRefreshType() {
			return refreshType;
		}
	}

}