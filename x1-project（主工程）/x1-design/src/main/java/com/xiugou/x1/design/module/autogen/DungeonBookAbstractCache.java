package com.xiugou.x1.design.module.autogen;


public abstract class DungeonBookAbstractCache<T extends DungeonBookAbstractCache.DungeonBookCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "F副本工具书表_DungeonBook";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class DungeonBookCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 书本ID
		 */
		protected int id;
		/**
		 * 技能ID
		 */
		protected int skillId;
		/**
		 * 权重
		 */
		protected int weight;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSkillId() {
			return skillId;
		}
		public int getWeight() {
			return weight;
		}
	}

}