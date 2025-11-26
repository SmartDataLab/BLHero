package com.xiugou.x1.design.module.autogen;


public abstract class AccessAbstractCache<T extends AccessAbstractCache.AccessCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "H获取途径_Access";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class AccessCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 获取途径ID
		 */
		protected int id;
		/**
		 * 获取途径名称
		 */
		protected String name;
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
	}

}