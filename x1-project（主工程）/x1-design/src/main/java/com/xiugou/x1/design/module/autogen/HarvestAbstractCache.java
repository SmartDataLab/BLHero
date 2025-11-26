package com.xiugou.x1.design.module.autogen;


public abstract class HarvestAbstractCache<T extends HarvestAbstractCache.HarvestCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "S收割资源类型表_Harvest";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HarvestCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 收割资源ID
		 */
		protected int id;
		/**
		 * 资源名称
		 */
		protected String name;
		/**
		 * 刷新时间秒
		 */
		protected int refreshTime;
		/**
		 * 需收割次数
		 */
		protected int needNum;
		/**
		 * 收割产出
		 */
		protected com.xiugou.x1.design.struct.RewardThing produce;
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
		public int getRefreshTime() {
			return refreshTime;
		}
		public int getNeedNum() {
			return needNum;
		}
		public com.xiugou.x1.design.struct.RewardThing getProduce() {
			return produce;
		}
	}

}