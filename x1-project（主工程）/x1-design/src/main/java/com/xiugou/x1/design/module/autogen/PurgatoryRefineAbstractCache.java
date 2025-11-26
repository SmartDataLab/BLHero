package com.xiugou.x1.design.module.autogen;


public abstract class PurgatoryRefineAbstractCache<T extends PurgatoryRefineAbstractCache.PurgatoryRefineCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L炼狱洗练消耗_PurgatoryRefine";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PurgatoryRefineCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 洗练次数
		 */
		protected int times;
		/**
		 * 通关消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing passCost;
		/**
		 * 消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing cost;
		@Override
		public int id() {
			return times;
		}
		public int getTimes() {
			return times;
		}
		public com.xiugou.x1.design.struct.CostThing getPassCost() {
			return passCost;
		}
		public com.xiugou.x1.design.struct.CostThing getCost() {
			return cost;
		}
	}

}