package com.xiugou.x1.design.module.autogen;


public abstract class SceneStageAbstractCache<T extends SceneStageAbstractCache.SceneStageCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C场景难度阶段表_SceneStage";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdStageIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引sceneIdStageIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdStageIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = sceneIdStageIndex.get(data.getSceneId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				sceneIdStageIndex.put(data.getSceneId(), layer1Map);
			}
			layer1Map.put(data.getStage(), data);
		}
		this.sceneIdStageIndex = sceneIdStageIndex;
	}

	public final T getInSceneIdStageIndex(int sceneId, int stage) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdStageIndex.get(sceneId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneStageCache.getInSceneIdStageIndex", sceneId, stage);
		}
		T t = layer1Map.get(stage);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneStageCache.getInSceneIdStageIndex", sceneId, stage);
		}
		return t;
	}

	public final T findInSceneIdStageIndex(int sceneId, int stage) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdStageIndex.get(sceneId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(stage);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SceneStageCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 场景ID
		 */
		protected int sceneId;
		/**
		 * 难度阶段
		 */
		protected int stage;
		/**
		 * 首通奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> firstRewards;
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
		public int getStage() {
			return stage;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFirstRewards() {
			return firstRewards;
		}
	}

}