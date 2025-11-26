/**
 * 
 */
package org.gaming.prefab;

/**
 * @author YY
 * 游戏流水事件
 */
public interface IGameCause {

	int getCode();
	String getDesc();
	boolean isOverLimit();
}
