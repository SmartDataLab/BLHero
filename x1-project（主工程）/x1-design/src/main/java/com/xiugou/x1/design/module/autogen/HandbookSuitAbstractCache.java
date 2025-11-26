package com.xiugou.x1.design.module.autogen;


public abstract class HandbookSuitAbstractCache<T extends HandbookSuitAbstractCache.HandbookSuitCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T图鉴套装属性表_HandbookSuit";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HandbookSuitCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 加成属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attrs;
		/**
		 * 备注
		 */
		protected String describe;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttrs() {
			return attrs;
		}
		public String getDescribe() {
			return describe;
		}
	}

}