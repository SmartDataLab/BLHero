package com.xiugou.x1.design.module.autogen;


public abstract class DungeonAbstractCache<T extends DungeonAbstractCache.DungeonCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M秘境副本配置表_Dungeon";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class DungeonCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 秘境副本ID
		 */
		protected int id;
		/**
		 * 秘境副本名字
		 */
		protected String name;
		/**
		 * 秘境类型
		 */
		protected int type;
		/**
		 * 大陆ID
		 */
		protected int mainlineId;
		/**
		 * 推荐战力
		 */
		protected int fighting;
		/**
		 * 进入消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing enterCost;
		/**
		 * 关联场景ID
		 */
		protected int sceneId;
		/**
		 * 首通小怪掉落
		 */
		protected java.util.List<Integer> firstDrop;
		/**
		 * 首通boss掉落
		 */
		protected java.util.List<Integer> firstBossDrop;
		/**
		 * 非首通小怪掉落
		 */
		protected java.util.List<Integer> drop;
		/**
		 * 非首通boss掉落
		 */
		protected java.util.List<Integer> bossDrop;
		/**
		 * 需要等级
		 */
		protected int needLevel;
		/**
		 * 前置副本ID
		 */
		protected int precopy;
		/**
		 * 援助伙伴
		 */
		protected java.util.List<Integer> aid;
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
		public int getType() {
			return type;
		}
		public int getMainlineId() {
			return mainlineId;
		}
		public int getFighting() {
			return fighting;
		}
		public com.xiugou.x1.design.struct.CostThing getEnterCost() {
			return enterCost;
		}
		public int getSceneId() {
			return sceneId;
		}
		public java.util.List<Integer> getFirstDrop() {
			return firstDrop;
		}
		public java.util.List<Integer> getFirstBossDrop() {
			return firstBossDrop;
		}
		public java.util.List<Integer> getDrop() {
			return drop;
		}
		public java.util.List<Integer> getBossDrop() {
			return bossDrop;
		}
		public int getNeedLevel() {
			return needLevel;
		}
		public int getPrecopy() {
			return precopy;
		}
		public java.util.List<Integer> getAid() {
			return aid;
		}
	}

}