package com.xiugou.x1.design.module.autogen;


public abstract class NpcModelAbstractCache<T extends NpcModelAbstractCache.NpcModelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "NNpc模型表_NpcModel";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class NpcModelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * npc模型ID
		 */
		protected int id;
		/**
		 * npc名字
		 */
		protected String name;
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
	}

}