package com.xiugou.x1.design.module.autogen;


public abstract class ArenaDailyRewardAbstractCache<T extends ArenaDailyRewardAbstractCache.ArenaDailyRewardCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J竞技场每日奖励表_ArenaDailyReward";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ArenaDailyRewardCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 排名上
		 */
		protected int rankUp;
		/**
		 * 排名下
		 */
		protected int rankDown;
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
		public int getRankUp() {
			return rankUp;
		}
		public int getRankDown() {
			return rankDown;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}