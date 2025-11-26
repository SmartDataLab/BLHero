/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

/**
 * @author YY
 * 眩晕状态
 */
public class StunStatus extends AbstractStatus {

	@Override
	public StatusEnum statusTag() {
		return StatusEnum.STUN;
	}
	
	public boolean canAction() {
		return false;
	}
}
