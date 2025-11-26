/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.constant;

/**
 * @author YY
 *
 */
public enum RechargeStatus {
	UNHANDLE(0), //待处理
	SUCCESS(1),//金额一致发货成功
	FAIL(2),//失败
	;
	private final int type;
	private RechargeStatus(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
}
