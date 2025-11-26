package com.xiugou.x1.design.module.autogen;


public abstract class PlayerLevelAbstractCache<T extends PlayerLevelAbstractCache.PlayerLevelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J角色等级表_PlayerLevel";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PlayerLevelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 等级
		 */
		protected int id;
		/**
		 * 升到下一级需要的经验
		 */
		protected int exp;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getExp() {
			return exp;
		}
	}

}