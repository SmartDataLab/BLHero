package com.xiugou.x1.design.module.autogen;


public abstract class RuneAbstractCache<T extends RuneAbstractCache.RuneCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z真言表_Rune";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class RuneCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 真言ID
		 */
		protected int id;
		/**
		 * 真言穿戴部位
		 */
		protected int part;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 作为强化素材时的经验
		 */
		protected int materialExp;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getPart() {
			return part;
		}
		public String getName() {
			return name;
		}
		public int getMaterialExp() {
			return materialExp;
		}
	}

}