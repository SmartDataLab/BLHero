package com.xiugou.x1.design.module.autogen;


public abstract class FunctionOpenAbstractCache<T extends FunctionOpenAbstractCache.FunctionOpenCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "G功能开启表_FunctionOpen";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class FunctionOpenCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 功能ID
		 */
		protected int id;
		/**
		 * 开启条件
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> openCondition;
		/**
		 * 屏蔽充值
		 */
		protected int hide;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getOpenCondition() {
			return openCondition;
		}
		public int getHide() {
			return hide;
		}
	}

}