/**
 * 
 */
package com.xiugou.x1.battle.config;

/**
 * @author YY
 *
 */
public interface ISkillLevelConfigCache {
	
	ISkillLevelConfig getConfig(ISkillCfg skillCfg);
	
	ISkillLevelConfig getConfig(int skillId, int level);
}
