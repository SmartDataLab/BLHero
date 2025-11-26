package com.xiugou.x1.design.module.autogen;


public abstract class BubbleAbstractCache<T extends BubbleAbstractCache.BubbleCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Q气泡文本配置表_Bubble";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class BubbleCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 气泡ID
		 */
		protected int id;
		/**
		 * 对象类型
		 */
		protected int objectType;
		/**
		 * 播放对象
		 */
		protected int playObject;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getObjectType() {
			return objectType;
		}
		public int getPlayObject() {
			return playObject;
		}
	}

}