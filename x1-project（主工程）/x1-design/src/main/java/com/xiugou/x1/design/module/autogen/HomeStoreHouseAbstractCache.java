package com.xiugou.x1.design.module.autogen;


public abstract class HomeStoreHouseAbstractCache<T extends HomeStoreHouseAbstractCache.HomeStoreHouseCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J家园仓库容量表_HomeStoreHouse";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HomeStoreHouseCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 等级
		 */
		protected int id;
		/**
		 * 肉容量
		 */
		protected long meat;
		/**
		 * 木容量
		 */
		protected long wood;
		/**
		 * 矿容量
		 */
		protected long mine;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public long getMeat() {
			return meat;
		}
		public long getWood() {
			return wood;
		}
		public long getMine() {
			return mine;
		}
	}

}