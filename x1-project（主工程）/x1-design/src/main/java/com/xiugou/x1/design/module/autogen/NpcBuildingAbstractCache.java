package com.xiugou.x1.design.module.autogen;


public abstract class NpcBuildingAbstractCache<T extends NpcBuildingAbstractCache.NpcBuildingCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J建筑物表_NpcBuilding";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class NpcBuildingCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 建筑物ID
		 */
		protected int id;
		/**
		 * 建筑类型
		 */
		protected int type;
		/**
		 * 前置建筑ID
		 */
		protected int preBuildingId;
		/**
		 * 功能表id
		 */
		protected int functionId;
		/**
		 * 开启条件
		 */
		protected int openCondition;
		/**
		 * 解锁消耗道具
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> costThing;
		/**
		 * 建筑等级
		 */
		protected int level;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getType() {
			return type;
		}
		public int getPreBuildingId() {
			return preBuildingId;
		}
		public int getFunctionId() {
			return functionId;
		}
		public int getOpenCondition() {
			return openCondition;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getCostThing() {
			return costThing;
		}
		public int getLevel() {
			return level;
		}
	}

}