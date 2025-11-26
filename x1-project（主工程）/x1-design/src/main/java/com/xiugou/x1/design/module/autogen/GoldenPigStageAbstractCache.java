package com.xiugou.x1.design.module.autogen;


public abstract class GoldenPigStageAbstractCache<T extends GoldenPigStageAbstractCache.GoldenPigStageCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "H黄金猪挑战难度表_GoldenPigStage";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class GoldenPigStageCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 难度
		 */
		protected int id;
		/**
		 * 通关奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		/**
		 * 怪物
		 */
		protected com.xiugou.x1.design.struct.Monster monsters;
		/**
		 * 刷怪点类型（0固定 1随机）
		 */
		protected int refreshType;
		/**
		 * 关联场景ID
		 */
		protected int sceneId;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public com.xiugou.x1.design.struct.RewardThing getReward() {
			return reward;
		}
		public com.xiugou.x1.design.struct.Monster getMonsters() {
			return monsters;
		}
		public int getRefreshType() {
			return refreshType;
		}
		public int getSceneId() {
			return sceneId;
		}
	}

}