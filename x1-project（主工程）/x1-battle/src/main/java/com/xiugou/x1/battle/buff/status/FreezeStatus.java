/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

/**
 * @author YY
 * 冰冻状态
 */
public class FreezeStatus extends AbstractStatus {

	@Override
	public StatusEnum statusTag() {
		return StatusEnum.FREEZED;
	}
	
	public boolean canAction() {
		return false;
	}
	
}
