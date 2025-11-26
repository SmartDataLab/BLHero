package com.xiugou.x1.design.module.autogen;


public abstract class EquipRateAbstractCache<T extends EquipRateAbstractCache.EquipRateCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z装备品质概率表_EquipRate";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class EquipRateCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 随机权重
		 */
		protected int weight;
		/**
		 * 属性下限
		 */
		protected int attrDown;
		/**
		 * 属性上限
		 */
		protected int attrUp;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getWeight() {
			return weight;
		}
		public int getAttrDown() {
			return attrDown;
		}
		public int getAttrUp() {
			return attrUp;
		}
	}

}