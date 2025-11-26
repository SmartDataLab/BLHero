/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

/**
 * @author YY
 *
 */
public class FloatingStatus extends AbstractStatus {

	@Override
	public StatusEnum statusTag() {
		return StatusEnum.FLOATING;
	}

	@Override
	public boolean canAction() {
		return false;
	}
}
