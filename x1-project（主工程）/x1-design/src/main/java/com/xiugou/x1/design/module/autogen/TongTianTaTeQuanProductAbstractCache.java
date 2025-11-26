package com.xiugou.x1.design.module.autogen;


public abstract class TongTianTaTeQuanProductAbstractCache<T extends TongTianTaTeQuanProductAbstractCache.TongTianTaTeQuanProductCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1004特权系统通天塔充值档次_TongTianTaTeQuanProduct";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class TongTianTaTeQuanProductCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 充值商品ID
		 */
		protected int id;
		/**
		 * 塔类型
		 */
		protected int towerType;
		/**
		 * 奖励期数
		 */
		protected int round;
		/**
		 * 购买商品需要VIP等级
		 */
		protected int vipLevel;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getTowerType() {
			return towerType;
		}
		public int getRound() {
			return round;
		}
		public int getVipLevel() {
			return vipLevel;
		}
	}

}