package com.xiugou.x1.design.module.autogen;


public abstract class HeroTacticsAbstractCache<T extends HeroTacticsAbstractCache.HeroTacticsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄流派表_HeroTactics";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HeroTacticsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 流派类型
		 */
		protected int id;
		/**
		 * 流派图标
		 */
		protected String icon;
		/**
		 * 流派描述
		 */
		protected String describe;
		/**
		 * 核心伙伴id
		 */
		protected int heroIdentity;
		/**
		 * 羁绊伙伴列表
		 */
		protected String heroList;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getIcon() {
			return icon;
		}
		public String getDescribe() {
			return describe;
		}
		public int getHeroIdentity() {
			return heroIdentity;
		}
		public String getHeroList() {
			return heroList;
		}
	}

}