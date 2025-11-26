package com.xiugou.x1.design.module.autogen;


public abstract class EquipAbstractCache<T extends EquipAbstractCache.EquipCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z装备表_Equip";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class EquipCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 装备ID
		 */
		protected int equipId;
		/**
		 * 装备大类
		 */
		protected int bigType;
		/**
		 * 装备小类
		 */
		protected int type;
		/**
		 * 装备名称
		 */
		protected String name;
		/**
		 * 装备品质
		 */
		protected int quality;
		/**
		 * 等级限制
		 */
		protected int level;
		/**
		 * 装备基础属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attrs;
		/**
		 * 附加属性库
		 */
		protected int attrRep;
		/**
		 * 是否需要鉴定
		 */
		protected int appraise;
		/**
		 * 基础评分
		 */
		protected int score;
		/**
		 * 分解获得
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> smeltReward;
		/**
		 * 套装ID
		 */
		protected int suitId;
		@Override
		public int id() {
			return equipId;
		}
		public int getEquipId() {
			return equipId;
		}
		public int getBigType() {
			return bigType;
		}
		public int getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public int getQuality() {
			return quality;
		}
		public int getLevel() {
			return level;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttrs() {
			return attrs;
		}
		public int getAttrRep() {
			return attrRep;
		}
		public int getAppraise() {
			return appraise;
		}
		public int getScore() {
			return score;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getSmeltReward() {
			return smeltReward;
		}
		public int getSuitId() {
			return suitId;
		}
	}

}