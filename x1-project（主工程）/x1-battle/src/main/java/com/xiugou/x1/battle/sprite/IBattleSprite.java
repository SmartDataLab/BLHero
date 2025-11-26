/**
 * 
 */
package com.xiugou.x1.battle.sprite;

import com.xiugou.x1.battle.attr.IBattleAttr;

/**
 * @author YY
 *
 */
public interface IBattleSprite {
	int getConfigId();
	int getIdentity();
	int getLevel();
	
	IBattleAttr getPanelAttr();
}
