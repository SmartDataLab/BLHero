/**
 * 
 */
package com.xiugou.x1.battle.result.sprite;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.battle.result.IActionEvent;

/**
 * @author YY
 *
 */
public class SpriteDeadEvent implements IActionEvent {
	private int spriteId;
	private long rebornTime;
	private List<Integer> drops = new ArrayList<>(5);

	@Override
	public String buildLog() {
		return String.format("精灵%s死亡，重生时间%s", spriteId, rebornTime);
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public List<Integer> getDrops() {
		return drops;
	}

	public void setDrops(List<Integer> drops) {
		this.drops = drops;
	}

	public long getRebornTime() {
		return rebornTime;
	}

	public void setRebornTime(long rebornTime) {
		this.rebornTime = rebornTime;
	}
	
}
