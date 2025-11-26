package com.xiugou.x1.design.module.autogen;


public abstract class PurgatoryAbstractCache<T extends PurgatoryAbstractCache.PurgatoryCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L炼狱轮回_Purgatory";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PurgatoryCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 炼狱阶数
		 */
		protected int id;
		/**
		 * 关联场景ID
		 */
		protected int sceneId;
		/**
		 * 加成掉落
		 */
		protected java.util.List<Integer> boostDrop;
		/**
		 * 加成Boss掉落
		 */
		protected java.util.List<Integer> bossDrop;
		/**
		 * 属性库ID
		 */
		protected int attrStash;
		/**
		 * 加成消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing plusCost;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSceneId() {
			return sceneId;
		}
		public java.util.List<Integer> getBoostDrop() {
			return boostDrop;
		}
		public java.util.List<Integer> getBossDrop() {
			return bossDrop;
		}
		public int getAttrStash() {
			return attrStash;
		}
		public com.xiugou.x1.design.struct.CostThing getPlusCost() {
			return plusCost;
		}
	}

}