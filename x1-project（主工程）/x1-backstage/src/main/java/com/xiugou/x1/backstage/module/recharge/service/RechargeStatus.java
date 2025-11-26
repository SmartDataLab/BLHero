/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

/**
 * @author YY
 *
 */
public enum RechargeStatus {
	NONE,
	NOT_CHECK,	//没有通过全部检查
	SUCCESS,	//成功
	FAIL,		//失败
	REPEATED,	//订单重复
}
