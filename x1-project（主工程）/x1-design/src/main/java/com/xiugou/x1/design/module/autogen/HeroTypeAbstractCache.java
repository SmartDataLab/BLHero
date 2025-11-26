package com.xiugou.x1.design.module.autogen;


public abstract class HeroTypeAbstractCache<T extends HeroTypeAbstractCache.HeroTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄类型表_HeroType";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HeroTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 标识
		 */
		protected int id;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 种族类型
		 */
		protected int element;
		/**
		 * 职业类型
		 */
		protected java.util.List<Integer> career;
		/**
		 * 品质
		 */
		protected int quality;
		/**
		 * 英雄头像
		 */
		protected String head;
		/**
		 * 立绘资源
		 */
		protected int inset;
		/**
		 * 头像资源
		 */
		protected String portrait;
		/**
		 * 模型资源id
		 */
		protected int modelId;
		/**
		 * 放大系数
		 */
		protected int scale;
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
		public int getElement() {
			return element;
		}
		public java.util.List<Integer> getCareer() {
			return career;
		}
		public int getQuality() {
			return quality;
		}
		public String getHead() {
			return head;
		}
		public int getInset() {
			return inset;
		}
		public String getPortrait() {
			return portrait;
		}
		public int getModelId() {
			return modelId;
		}
		public int getScale() {
			return scale;
		}
	}

}