/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import com.xiugou.x1.battle.config.ISkillLevelConfig;

/**
 * @author YY
 *
 */
public class DefaultActiveSkill extends ActiveSkill<EmptyStruct> {

	public DefaultActiveSkill(ISkillLevelConfig config) {
		this.skillCfg = config;
	}
}
