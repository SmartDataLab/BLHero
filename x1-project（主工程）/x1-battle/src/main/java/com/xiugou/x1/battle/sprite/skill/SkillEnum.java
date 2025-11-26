/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.battle.config.ISkillLevelConfig;
import com.xiugou.x1.battle.constant.SkillType;

/**
 * @author YY
 *
 */
public abstract class SkillEnum {

	private static Map<Integer, SkillEnum> skillMap = new HashMap<>();
	public static Skill<?> newSkill(ISkillLevelConfig config) {
		SkillEnum skillEnum = skillMap.get(config.getSkillId());
		if(skillEnum == null) {
			if(config.getSkillType() == SkillType.NORMAL.getValue() || config.getSkillType() == SkillType.ACTIVE.getValue()) {
				return new DefaultActiveSkill(config);
			} else {
				return new DefaultPassiveSkill(config);
			}
		}
		try {
			Skill<?> skill = skillEnum.skillClazz.newInstance();
			skill.setSkillCfg(config);
			return skill;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	private SkillEnum(int skillId, Class<? extends Skill<?>> skillClazz) {
		this.skillClazz = skillClazz;
		skillMap.put(skillId, this);
	}
	
	private Class<? extends Skill<?>> skillClazz;
	
}
