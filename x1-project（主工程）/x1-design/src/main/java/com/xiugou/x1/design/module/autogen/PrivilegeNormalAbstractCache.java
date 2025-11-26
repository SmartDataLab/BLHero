package com.xiugou.x1.design.module.autogen;


public abstract class PrivilegeNormalAbstractCache<T extends PrivilegeNormalAbstractCache.PrivilegeNormalCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P普通特权表_PrivilegeNormal";
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
			throw new org.gaming.design.exception.DesignNotFoundException("PrivilegeNormalCache.getInRechargeIdIndex", rechargeId);
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



	public static class PrivilegeNormalCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 特权ID
		 */
		protected int id;
		/**
		 * 充值商品表ID
		 */
		protected int rechargeId;
		/**
		 * 持续天数
		 */
		protected int days;
		/**
		 * 是否开启
		 */
		protected int active;
		/**
		 * 充值奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rechargeRewards;
		/**
		 * 每日奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> dailyRewards;
		/**
		 * 充值自选奖励[废弃]
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> chooseRewards;
		/**
		 * 框的背景
		 */
		protected String itemBg;
		/**
		 * 奖励框的背景
		 */
		protected String itemlistBg;
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
		public int getDays() {
			return days;
		}
		public int getActive() {
			return active;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRechargeRewards() {
			return rechargeRewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getDailyRewards() {
			return dailyRewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getChooseRewards() {
			return chooseRewards;
		}
		public String getItemBg() {
			return itemBg;
		}
		public String getItemlistBg() {
			return itemlistBg;
		}
	}

}