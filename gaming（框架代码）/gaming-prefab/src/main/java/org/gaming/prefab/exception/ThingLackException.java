/**
 * 
 */
package org.gaming.prefab.exception;

import java.util.Arrays;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;

/**
 * @author YY
 *
 */
public class ThingLackException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final IGameCause gameCasuse;
	
	public ThingLackException(IGameCause gameCasuse, ITipCause exceptionCause, Object... params) {
		super(exceptionCause, params);
		this.gameCasuse = gameCasuse;
	}
	
	@Override
	public String getMessage() {
		return gameCasuse.getClass().getSimpleName() + "." + gameCasuse + " " + this.getExceptionCause().getDesc() + " params:" + Arrays.toString(this.getParams());
	}
}
