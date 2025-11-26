/**
 * 
 */
package com.xiugou.x1.battle.buff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.xiugou.x1.battle.config.IBuffConfig;

/**
 * @author YY
 *
 */
public abstract class BuffEffect {

	private static Map<Integer, BuffEffect> enumMap = new HashMap<>();
	public static BuffEffect valueOf(IBuffConfig buffConfig) {
		return enumMap.get(buffConfig.getBuffEnum());
	}
	
	private final String type;
	private final String desc;
	private final int buffId;
	//buff类型，1buff，2debuff，3控制类，4特殊效果，5改变行为
//	private final int buffType;
	public final Supplier<Buff> supplier;

	private BuffEffect(String type, String desc, int buffId, Supplier<Buff> supplier) {
		this.type = type;
		this.desc = desc;
		this.buffId = buffId;
		this.supplier = supplier;
		enumMap.put(buffId, this);
	}
	
	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public int getBuffId() {
		return buffId;
	}
	
	
	public static BuffEffect CHANGE_ATTR = 		new BuffEffect(	"CHANGE_ATTR", 		"增减属性", 		101, () -> new ChangeAttr()) {};
	public static BuffEffect CHANGE_JOB_ATTR = 	new BuffEffect(	"CHANGE_JOB_ATTR",	"增减指定职业属性", 102, () -> new ChangeJobAttr()) {};
	
	public static BuffEffect BURNS = 			new BuffEffect(	"BURNS", 			"燃烧", 			201, () -> new Burns()) {};
	public static BuffEffect POISON = 			new BuffEffect(	"POISON", 			"中毒", 			202, () -> new Poison()) {};
	public static BuffEffect BLEED = 			new BuffEffect(	"BLEED", 			"流血", 			203, () -> new Bleed()) {};
	
	public static BuffEffect FREEZING = 		new BuffEffect(	"FREEZING", 		"冻伤", 			301, () -> new Freezing()) {};
	public static BuffEffect FREEZED = 			new BuffEffect(	"FREEZED", 			"冰冻", 			302, () -> new Freezed()) {};
	public static BuffEffect STUN = 			new BuffEffect(	"STUN",				"眩晕", 			303, () -> new Stun()) {};
	public static BuffEffect FLOATING = 		new BuffEffect(	"FLOATING", 		"浮空", 			304, () -> new Floating()) {};
	public static BuffEffect TAUNT = 			new BuffEffect(	"TAUNT", 			"嘲讽", 			305, () -> new Taunt()) {};
	public static BuffEffect INVICTUS = 		new BuffEffect(	"INVICTUS", 		"无敌", 			306, () -> new Invictus()) {};
	
	public static BuffEffect REJUVENATE = 		new BuffEffect(	"REJUVENATE", 		"持续治疗", 		401, () -> new Rejuvenate()) {};
	
}
