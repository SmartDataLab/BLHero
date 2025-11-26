package com.xiugou.x1.design.module.autogen;


public abstract class AchievementPointsAbstractCache<T extends AchievementPointsAbstractCache.AchievementPointsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C成就点数奖励表_AchievementPoints";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class AchievementPointsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 奖励ID
		 */
		protected int id;
		/**
		 * 需要点数
		 */
		protected int needPoint;
		/**
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getNeedPoint() {
			return needPoint;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}