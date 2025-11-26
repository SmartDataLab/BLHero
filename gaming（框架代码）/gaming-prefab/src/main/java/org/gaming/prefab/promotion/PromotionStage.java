/**
 * 
 */
package org.gaming.prefab.promotion;

/**
 * @author YY
 *
 */
public enum PromotionStage {
	/**
	 * 空闲状态
	 */
	IDLE(0),
	/**
	 * 进行状态
	 */
	RUNNING(1),
	/**
	 * 沉寂状态
	 */
	STILL(2),
	/**
	 * 结束状态
	 */
	END(3),
	/**
	 * 停止状态
	 */
	STOP(4),
	;
	
	private final int value;
	private PromotionStage(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
