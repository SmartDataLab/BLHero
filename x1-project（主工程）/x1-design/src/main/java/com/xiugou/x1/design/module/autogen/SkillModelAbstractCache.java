package com.xiugou.x1.design.module.autogen;


public abstract class SkillModelAbstractCache<T extends SkillModelAbstractCache.SkillModelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "2技能效果表_SkillModel";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class SkillModelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 技能表现ID
		 */
		protected int id;
		/**
		 * 偏移值
		 */
		protected java.util.List<Integer> offset;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public java.util.List<Integer> getOffset() {
			return offset;
		}
	}

}