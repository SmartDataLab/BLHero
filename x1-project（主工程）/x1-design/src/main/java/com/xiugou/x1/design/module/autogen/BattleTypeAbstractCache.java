package com.xiugou.x1.design.module.autogen;


public abstract class BattleTypeAbstractCache<T extends BattleTypeAbstractCache.BattleTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z战斗类型表_BattleType";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class BattleTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * ID
		 */
		protected int id;
		/**
		 * 名字
		 */
		protected String name;
		/**
		 * 默认的地图文件
		 */
		protected String mapData;
		/**
		 * 战斗时长毫秒
		 */
		protected int battleTime;
		/**
		 * 是否统计连杀数
		 */
		protected int sumKill;
		/**
		 * 是否可以自动战斗
		 */
		protected int canAuto;
		/**
		 * 每天获得免费门票
		 */
		protected com.xiugou.x1.design.struct.RewardThing freeTicket;
		/**
		 * 默认场景ID
		 */
		protected int defaultScene;
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
		public String getMapData() {
			return mapData;
		}
		public int getBattleTime() {
			return battleTime;
		}
		public int getSumKill() {
			return sumKill;
		}
		public int getCanAuto() {
			return canAuto;
		}
		public com.xiugou.x1.design.struct.RewardThing getFreeTicket() {
			return freeTicket;
		}
		public int getDefaultScene() {
			return defaultScene;
		}
	}

}