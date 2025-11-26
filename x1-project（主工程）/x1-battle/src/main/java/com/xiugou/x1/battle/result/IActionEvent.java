/**
 * 
 */
package com.xiugou.x1.battle.result;

/**
 * @author YY
 *
 */
public interface IActionEvent {
	
//	PbActionEventType event();
	
//	ByteString build();
	
	String buildLog();
	
	default boolean isEmpty() {
		return false;
	}
}
