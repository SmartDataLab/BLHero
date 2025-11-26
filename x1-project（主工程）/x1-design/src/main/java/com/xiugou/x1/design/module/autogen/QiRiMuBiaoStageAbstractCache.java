package com.xiugou.x1.design.module.autogen;


public abstract class QiRiMuBiaoStageAbstractCache<T extends QiRiMuBiaoStageAbstractCache.QiRiMuBiaoStageCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1007七日目标阶段奖励_QiRiMuBiaoStage";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class QiRiMuBiaoStageCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 阶段ID
		 */
		protected int id;
		/**
		 * 阶段目标任务数
		 */
		protected int condition;
		/**
		 * 任务奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getCondition() {
			return condition;
		}
		public com.xiugou.x1.design.struct.RewardThing getReward() {
			return reward;
		}
	}

}