package com.xiugou.x1.design.module.autogen;


public abstract class MonsterAbstractCache<T extends MonsterAbstractCache.MonsterCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "G怪物表_Monster";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class MonsterCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 怪物ID
		 */
		protected int id;
		/**
		 * 怪物名称(程序字)
		 */
		protected String name;
		/**
		 * 怪物模型
		 */
		protected int identity;
		/**
		 * 放大系数
		 */
		protected int scale;
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 类型
		 */
		protected int type;
		/**
		 * 怪物属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attribute;
		/**
		 * 最高扣血
		 */
		protected int maxHurt;
		/**
		 * 刷新时间秒
		 */
		protected int refreshTime;
		/**
		 * 寻敌范围
		 */
		protected int huntingRange;
		/**
		 * 追击范围
		 */
		protected int chaseRange;
		/**
		 * 脱战范围
		 */
		protected int outOfRange;
		/**
		 * 脱战回血效率
		 */
		protected String revitalize;
		/**
		 * 击杀固定产出
		 */
		protected com.xiugou.x1.design.struct.RewardThing produce;
		/**
		 * 随机掉落组
		 */
		protected java.util.List<Integer> randomProduce;
		/**
		 * 怪物技能
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> skills;
		/**
		 * 霸体状态
		 */
		protected String superArmor;
		/**
		 * 怪物战力
		 */
		protected long fighting;
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
		public int getIdentity() {
			return identity;
		}
		public int getScale() {
			return scale;
		}
		public int getLevel() {
			return level;
		}
		public int getType() {
			return type;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttribute() {
			return attribute;
		}
		public int getMaxHurt() {
			return maxHurt;
		}
		public int getRefreshTime() {
			return refreshTime;
		}
		public int getHuntingRange() {
			return huntingRange;
		}
		public int getChaseRange() {
			return chaseRange;
		}
		public int getOutOfRange() {
			return outOfRange;
		}
		public String getRevitalize() {
			return revitalize;
		}
		public com.xiugou.x1.design.struct.RewardThing getProduce() {
			return produce;
		}
		public java.util.List<Integer> getRandomProduce() {
			return randomProduce;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getSkills() {
			return skills;
		}
		public String getSuperArmor() {
			return superArmor;
		}
		public long getFighting() {
			return fighting;
		}
	}

}