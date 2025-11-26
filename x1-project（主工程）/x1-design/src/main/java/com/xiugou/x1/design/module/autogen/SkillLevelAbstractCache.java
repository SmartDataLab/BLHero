package com.xiugou.x1.design.module.autogen;


public abstract class SkillLevelAbstractCache<T extends SkillLevelAbstractCache.SkillLevelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "2技能表_SkillLevel";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> skillIdLevelIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引skillIdLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> skillIdLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = skillIdLevelIndex.get(data.getSkillId());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				skillIdLevelIndex.put(data.getSkillId(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.skillIdLevelIndex = skillIdLevelIndex;
	}

	public final T getInSkillIdLevelIndex(int skillId, int level) {
		java.util.HashMap<Integer, T> layer1Map = skillIdLevelIndex.get(skillId);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SkillLevelCache.getInSkillIdLevelIndex", skillId, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SkillLevelCache.getInSkillIdLevelIndex", skillId, level);
		}
		return t;
	}

	public final T findInSkillIdLevelIndex(int skillId, int level) {
		java.util.HashMap<Integer, T> layer1Map = skillIdLevelIndex.get(skillId);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SkillLevelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 技能ID
4000000+伙伴ID*1000+技能顺序ID+技能等级
		 */
		protected int id;
		/**
		 * 技能组ID
		 */
		protected int skillId;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 技能标签
		 */
		protected int skillType;
		/**
		 * 技能等级
		 */
		protected int level;
		/**
		 * 效果类型
1、近战伤害
2、技能伤害
4、附加buff
		 */
		protected int dmgType;
		/**
		 * 效果参数
3、治疗：系数类型（1施法者攻击百分比；2目标生命上限百分比）
- 治疗系数在伤害比例字段配置
5、复活：系统类型（1施法者攻击百分比；2目标生命上限百分比）
- 治疗系数在伤害比例字段配置
6、弹射：子弹ID#弹射次数#伤害系数（3000表示30%，打击多少次填多少个）
7、子弹伤害：子弹ID#子弹类型（1矩形；2扇形）#子弹数量#射击方式（1平射；2抛物线）#范围参数（1间隔距离；2夹角）
		 */
		protected String specialEffect;
		/**
		 * 作用对象
1自己
2己方
3敌方
4己方除自己
99.无(不调用相应技能指令；处理技能1替代普攻类伙伴)
		 */
		protected int targetType;
		/**
		 * 攻击距离
技能攻击距离/像素
距离算施法者和目标的中心点距离
		 */
		protected int atkRange;
		/**
		 * 动作名称
0：无动作
1：普攻
2：技能1
3：技能2
		 */
		protected int actionId;
		/**
		 * 目标选择
1：距离最近的目标
2：以自身为中心
3：以目标为中心
4：以受击目标为中心
5：生命最低的单位（百分比）
6：生命最少的单位（生命值）
7：已死亡己方（复活技能使用）
		 */
		protected int selectType;
		/**
		 * 作用范围
格式：范围类型#类型参数
0：读攻击距离
1：圆形，配置格式：1#半径
2：矩形，配置格式：2#宽度#长度
3：扇形，配置格式：3#夹角度数#半径
		 */
		protected String rangeWay;
		/**
		 * 目标数量上限：


		 */
		protected int targetLimit;
		/**
		 * 常驻被动属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> nailAttr;
		/**
		 * 战斗中被动属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> passiveAttr;
		/**
		 * 伤害固定值
		 */
		protected int fixValue;
		/**
		 * 伤害比例
		 */
		protected int skillFactor;
		/**
		 * 动作时间：
动作持续时间，与伤害无关；施法动画的长度

		 */
		protected int actionTime;
		/**
		 * 技能时间毫秒：
技能时间不能小于动作时间，否则会导致技能一致持续释放

如果特效播放时间过短，就去看下特效持续时间是否正确
用攻击帧时间+特效时间


		 */
		protected int skillTime;
		/**
		 * 技能冷却
		 */
		protected int cdTime;
		/**
		 * 额外触发条件
		 */
		protected com.xiugou.x1.design.struct.Keyv extraCd;
		/**
		 * 移动是否停止施法
		 */
		protected int cease;
		/**
		 * buff附加时间
1：出手后
2：出手前
		 */
		protected int buffMoment;
		/**
		 * 附加buff：格式：buff#概率#是否每个帧都附加Buff#目标类型#目标数量|....

目标类型：0与技能目标相同；1自己；2己方；3敌方；4己方除自己
附加buff时间：-1每个攻击帧都会附近，大于0则指定攻击帧触发
目标数量，默认为0不用配置，与目标数量不同时配置


		 */
		protected java.util.List<com.xiugou.x1.battle.config.BuffTrigger> buffs;
		/**
		 * 战力
		 */
		protected long fighting;
		/**
		 * 是否显示技能预警
		 */
		protected int skillWarning;
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
		public String getName() {
			return name;
		}
		public int getSkillType() {
			return skillType;
		}
		public int getLevel() {
			return level;
		}
		public int getDmgType() {
			return dmgType;
		}
		public String getSpecialEffect() {
			return specialEffect;
		}
		public int getTargetType() {
			return targetType;
		}
		public int getAtkRange() {
			return atkRange;
		}
		public int getActionId() {
			return actionId;
		}
		public int getSelectType() {
			return selectType;
		}
		public String getRangeWay() {
			return rangeWay;
		}
		public int getTargetLimit() {
			return targetLimit;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getNailAttr() {
			return nailAttr;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getPassiveAttr() {
			return passiveAttr;
		}
		public int getFixValue() {
			return fixValue;
		}
		public int getSkillFactor() {
			return skillFactor;
		}
		public int getActionTime() {
			return actionTime;
		}
		public int getSkillTime() {
			return skillTime;
		}
		public int getCdTime() {
			return cdTime;
		}
		public com.xiugou.x1.design.struct.Keyv getExtraCd() {
			return extraCd;
		}
		public int getCease() {
			return cease;
		}
		public int getBuffMoment() {
			return buffMoment;
		}
		public java.util.List<com.xiugou.x1.battle.config.BuffTrigger> getBuffs() {
			return buffs;
		}
		public long getFighting() {
			return fighting;
		}
		public int getSkillWarning() {
			return skillWarning;
		}
	}

}