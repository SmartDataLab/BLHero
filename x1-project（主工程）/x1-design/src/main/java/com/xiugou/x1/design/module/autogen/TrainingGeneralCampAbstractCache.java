package com.xiugou.x1.design.module.autogen;


public abstract class TrainingGeneralCampAbstractCache<T extends TrainingGeneralCampAbstractCache.TrainingGeneralCampCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X训练营普通属性加成表_TrainingGeneralCamp";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class TrainingGeneralCampCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 训练等级
		 */
		protected int id;
		/**
		 * 角色等级
		 */
		protected int level;
		/**
		 * 属性字段名
		 */
		protected com.xiugou.x1.battle.config.Attr attr;
		/**
		 * 消耗道具
		 */
		protected com.xiugou.x1.design.struct.CostThing costThing;
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
		public com.xiugou.x1.battle.config.Attr getAttr() {
			return attr;
		}
		public com.xiugou.x1.design.struct.CostThing getCostThing() {
			return costThing;
		}
	}

}