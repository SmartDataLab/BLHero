/**
 * 
 */
package org.gaming.prefab.thing;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.MappedSuperclass;

/**
 * @author YY
 * 经验类日志
 */
@MappedSuperclass
public abstract class ExpThingLog extends NumberThingLog {
	@Column(name = "old_level", comment = "原等级")
	private int oldLevel;
	@Column(name = "curr_level", comment = "当前等级")
	private int currLevel;
	public void setOldLevel(int oldLevel) {
		this.oldLevel = oldLevel;
	}
	public void setCurrLevel(int currLevel) {
		this.currLevel = currLevel;
	}
	public int getOldLevel() {
		return oldLevel;
	}
	public int getCurrLevel() {
		return currLevel;
	}
}
