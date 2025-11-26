package com.xiugou.x1.design.module.autogen;


public abstract class ComboAttrAbstractCache<T extends ComboAttrAbstractCache.ComboAttrCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L连击Buff_ComboAttr";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ComboAttrCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 连击次数
		 */
		protected int num;
		/**
		 * buffId
		 */
		protected int buffId;
		/**
		 * 断连时间/毫秒
		 */
		protected long disconnect;
		@Override
		public int id() {
			return num;
		}
		public int getNum() {
			return num;
		}
		public int getBuffId() {
			return buffId;
		}
		public long getDisconnect() {
			return disconnect;
		}
	}

}