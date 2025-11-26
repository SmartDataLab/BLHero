package com.xiugou.x1.design.module.autogen;


public abstract class MainlineSceneAbstractCache<T extends MainlineSceneAbstractCache.MainlineSceneCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z主线场景表_MainlineScene";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class MainlineSceneCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 主线ID
		 */
		protected int id;
		/**
		 * 场景名称
		 */
		protected String name;
		/**
		 * 复活安全区ID
		 */
		protected int reviveZone;
		/**
		 * 关联场景
		 */
		protected int sceneId;
		/**
		 * 最后BOSS怪物ID
		 */
		protected int bossId;
		/**
		 * 下一主线场景ID
		 */
		protected int nextScene;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public int getReviveZone() {
			return reviveZone;
		}
		public int getSceneId() {
			return sceneId;
		}
		public int getBossId() {
			return bossId;
		}
		public int getNextScene() {
			return nextScene;
		}
	}

}