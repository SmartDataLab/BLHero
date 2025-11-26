package com.xiugou.x1.design.module.autogen;


public abstract class SceneNpcAbstractCache<T extends SceneNpcAbstractCache.SceneNpcCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z主线野外NPC表_SceneNpc";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdNpcIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引sceneIdNpcIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> sceneIdNpcIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = sceneIdNpcIdIndex.get(data.getSceneId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				sceneIdNpcIdIndex.put(data.getSceneId(), layer1Map);
			}
			layer1Map.put(data.getNpcId(), data);
		}
		this.sceneIdNpcIdIndex = sceneIdNpcIdIndex;
	}

	public final T getInSceneIdNpcIdIndex(int sceneId, int npcId) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdNpcIdIndex.get(sceneId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneNpcCache.getInSceneIdNpcIdIndex", sceneId, npcId);
		}
		T t = layer1Map.get(npcId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SceneNpcCache.getInSceneIdNpcIdIndex", sceneId, npcId);
		}
		return t;
	}

	public final T findInSceneIdNpcIdIndex(int sceneId, int npcId) {
		java.util.HashMap<Integer, T> layer1Map = sceneIdNpcIdIndex.get(sceneId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(npcId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SceneNpcCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 场景ID
		 */
		protected int sceneId;
		/**
		 * npcId
		 */
		protected int npcId;
		/**
		 * 完成消耗
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> questCosts;
		/**
		 * 完成奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> finishRewards;
		/**
		 * 所属区域ID
		 */
		protected int belongZoneId;
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
		public int getNpcId() {
			return npcId;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getQuestCosts() {
			return questCosts;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFinishRewards() {
			return finishRewards;
		}
		public int getBelongZoneId() {
			return belongZoneId;
		}
	}

}