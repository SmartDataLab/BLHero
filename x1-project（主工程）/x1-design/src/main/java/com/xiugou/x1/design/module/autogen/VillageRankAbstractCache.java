package com.xiugou.x1.design.module.autogen;


public abstract class VillageRankAbstractCache<T extends VillageRankAbstractCache.VillageRankCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X仙境排行奖励表_VillageRank";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class VillageRankCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 最高排名
		 */
		protected int rankMax;
		/**
		 * 最低排名
		 */
		protected int rankMin;
		/**
		 * 排行奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> reward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getRankMax() {
			return rankMax;
		}
		public int getRankMin() {
			return rankMin;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getReward() {
			return reward;
		}
	}

}