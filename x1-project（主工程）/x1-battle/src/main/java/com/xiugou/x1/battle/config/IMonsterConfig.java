/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

import com.xiugou.x1.battle.sprite.IBattleSprite;

/**
 * @author YY
 *
 */
public interface IMonsterConfig extends IBattleSprite {
	//配置ID
	int getId();
	int getRefreshTime();
	List<? extends ISkillCfg> getSkills();
	int getType();
}
