/**
 * 
 */
package pojo.xiugou.x1.pojo.log.mainline;

/**
 * @author YY
 *
 */
public enum MainlineTaskTiming {
	START(1),
	FINISH(2),
	;
	
	private final int value;
	private MainlineTaskTiming(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
}
