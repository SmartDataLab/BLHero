package com.xiugou.x1.design.module.autogen;


public abstract class BuffTypeAbstractCache<T extends BuffTypeAbstractCache.BuffTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "BBuff类型说明表_BuffType";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class BuffTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 名字
		 */
		protected String name;
		/**
		 * 枚举名
		 */
		protected String enumName;
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
		public String getEnumName() {
			return enumName;
		}
	}

}