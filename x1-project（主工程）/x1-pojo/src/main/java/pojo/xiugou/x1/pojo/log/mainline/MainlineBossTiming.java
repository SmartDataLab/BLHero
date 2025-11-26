/**
 * 
 */
package pojo.xiugou.x1.pojo.log.mainline;

/**
 * @author YY
 *
 */
public enum MainlineBossTiming {
	CHALLENGE(1),
	KILL(2),
	;
	
	private final int value;
	private MainlineBossTiming(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
