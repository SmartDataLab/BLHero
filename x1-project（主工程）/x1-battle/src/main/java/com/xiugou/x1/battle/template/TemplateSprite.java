/**
 * 
 */
package com.xiugou.x1.battle.template;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.sprite.IBattleSprite;

/**
 * @author YY
 *
 */
public class TemplateSprite implements IBattleSprite {
	private int identity;
	private int level;
	private BattleAttr panelAttr;
	private int pos;
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public BattleAttr getPanelAttr() {
		return panelAttr;
	}
	public void setPanelAttr(BattleAttr panelAttr) {
		this.panelAttr = panelAttr;
	}
	@Override
	public int getConfigId() {
		return 0;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
}
