/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

/**
 * @author YY
 * 无敌状态
 */
public class InvictusStatus extends AbstractStatus {

	@Override
	public StatusEnum statusTag() {
		return StatusEnum.INVICTUS;
	}
	
	public boolean immuneDamage() {
		return true;
	}
}
