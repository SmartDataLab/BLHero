package com.xiugou.x1.design.module.autogen;


public abstract class ExpLimitAbstractCache<T extends ExpLimitAbstractCache.ExpLimitCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J角色经验获取限制_ExpLimit";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ExpLimitCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 最小限制
		 */
		protected int limitMin;
		/**
		 * 最大限制
		 */
		protected int limitMax;
		/**
		 * 经验加成
		 */
		protected float expAdd;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getLimitMin() {
			return limitMin;
		}
		public int getLimitMax() {
			return limitMax;
		}
		public float getExpAdd() {
			return expAdd;
		}
	}

}