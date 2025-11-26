package com.xiugou.x1.design.module.autogen;


public abstract class TrainingExpertCampAbstractCache<T extends TrainingExpertCampAbstractCache.TrainingExpertCampCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X训练营高级属性加成表_TrainingExpertCamp";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class TrainingExpertCampCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 高级训练营等级
		 */
		protected int id;
		/**
		 * 需要角色等级
		 */
		protected int level;
		/**
		 * 加成类型
		 */
		protected int type;
		/**
		 * 加成增量
		 */
		protected int num;
		/**
		 * 加成详情
		 */
		protected int buff;
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
		public int getType() {
			return type;
		}
		public int getNum() {
			return num;
		}
		public int getBuff() {
			return buff;
		}
		public com.xiugou.x1.design.struct.CostThing getCostThing() {
			return costThing;
		}
	}

}