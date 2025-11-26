package com.xiugou.x1.design.module.autogen;


public abstract class ItemAbstractCache<T extends ItemAbstractCache.ItemCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "1道具表_Item";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ItemCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 道具ID
		 */
		protected int id;
		/**
		 * 道具名称
		 */
		protected String name;
		/**
		 * 道具逻辑类型
		 */
		protected int kind;
		/**
		 * 道具类型子类
		 */
		protected int subType;
		/**
		 * 逻辑类型参数
		 */
		protected int kindParam;
		/**
		 * 品质
		 */
		protected int quality;
		/**
		 * 使用类型
		 */
		protected int useType;
		/**
		 * 使用数据
		 */
		protected String useData;
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
		public int getKind() {
			return kind;
		}
		public int getSubType() {
			return subType;
		}
		public int getKindParam() {
			return kindParam;
		}
		public int getQuality() {
			return quality;
		}
		public int getUseType() {
			return useType;
		}
		public String getUseData() {
			return useData;
		}
	}

}