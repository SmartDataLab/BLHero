/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import com.xiugou.x1.battle.result.IActionEvent;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class SpriteRebornEvent implements IActionEvent {

	private Sprite sprite;
	

	@Override
	public String buildLog() {
		return String.format("精灵%s复活，复活血量%s", sprite.getId(), sprite.getHp());
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
