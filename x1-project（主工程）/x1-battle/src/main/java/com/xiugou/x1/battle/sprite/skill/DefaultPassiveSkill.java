/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.config.ISkillLevelConfig;

/**
 * @author YY
 *
 */
public class DefaultPassiveSkill extends PassiveSkill<EmptyStruct> {

	public DefaultPassiveSkill(ISkillLevelConfig config) {
		this.skillCfg = config;
	}
}
