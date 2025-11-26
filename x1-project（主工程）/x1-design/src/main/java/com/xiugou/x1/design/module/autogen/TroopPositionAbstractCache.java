package com.xiugou.x1.design.module.autogen;


public abstract class TroopPositionAbstractCache<T extends TroopPositionAbstractCache.TroopPositionCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "D队伍站位解锁表_TroopPosition";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class TroopPositionCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 站位数量
		 */
		protected int id;
		/**
		 * 角色等级
		 */
		protected int level;
		/**
		 * 开服天数
		 */
		protected int openDay;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getLevel() {
			return level;
		}
		public int getOpenDay() {
			return openDay;
		}
	}

}