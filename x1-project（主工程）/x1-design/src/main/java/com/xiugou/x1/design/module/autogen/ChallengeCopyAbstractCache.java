package com.xiugou.x1.design.module.autogen;


public abstract class ChallengeCopyAbstractCache<T extends ChallengeCopyAbstractCache.ChallengeCopyCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T挑战副本列表_ChallengeCopy";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ChallengeCopyCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
	}

}