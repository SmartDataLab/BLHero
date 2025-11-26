package com.xiugou.x1.design.module.autogen;


public abstract class ZhangLingNormalAbstractCache<T extends ZhangLingNormalAbstractCache.ZhangLingNormalCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z普通战令表_ZhangLingNormal";
	}
	protected java.util.HashMap<Integer, T> rechargeIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引rechargeIdIndex
		java.util.HashMap<Integer, T> rechargeIdIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			rechargeIdIndex.put(data.getRechargeId(), data);
		}
		this.rechargeIdIndex = rechargeIdIndex;
	}

	public final T getInRechargeIdIndex(int rechargeId) {
		T t = rechargeIdIndex.get(rechargeId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhangLingNormalCache.getInRechargeIdIndex", rechargeId);
		}
		return t;
	}

	public final T findInRechargeIdIndex(int rechargeId) {
		T t = rechargeIdIndex.get(rechargeId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class ZhangLingNormalCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 战令ID
		 */
		protected int id;
		/**
		 * 对应领取奖励ID
		 */
		protected int rechargeId;
		/**
		 *  ZhanLingExpID
		 */
		protected int expZLID;
		/**
		 * 框的背景
		 */
		protected String itemBg;
		/**
		 * 奖励框的背景
		 */
		protected String itemlistBg;
		/**
		 * 是否开启1开启2隐藏
		 */
		protected int active;
		/**
		 * 打开视图的名称
		 */
		protected String openModeView;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getRechargeId() {
			return rechargeId;
		}
		public int getExpZLID() {
			return expZLID;
		}
		public String getItemBg() {
			return itemBg;
		}
		public String getItemlistBg() {
			return itemlistBg;
		}
		public int getActive() {
			return active;
		}
		public String getOpenModeView() {
			return openModeView;
		}
	}

}