package com.xiugou.x1.design.module.autogen;


public abstract class QuickBuyingAbstractCache<T extends QuickBuyingAbstractCache.QuickBuyingCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "K快捷购买表_QuickBuying";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class QuickBuyingCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 道具ID
		 */
		protected int id;
		/**
		 * 每份数量
		 */
		protected int num;
		/**
		 * 购买消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing cost;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getNum() {
			return num;
		}
		public com.xiugou.x1.design.struct.CostThing getCost() {
			return cost;
		}
	}

}