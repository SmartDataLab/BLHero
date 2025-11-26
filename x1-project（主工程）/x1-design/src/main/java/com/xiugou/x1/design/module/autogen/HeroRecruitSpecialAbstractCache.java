package com.xiugou.x1.design.module.autogen;


public abstract class HeroRecruitSpecialAbstractCache<T extends HeroRecruitSpecialAbstractCache.HeroRecruitSpecialCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄特殊招募表_HeroRecruitSpecial";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HeroRecruitSpecialCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 手动刷新次数
		 */
		protected int id;
		/**
		 * 英雄池
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> heroPool;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getHeroPool() {
			return heroPool;
		}
	}

}