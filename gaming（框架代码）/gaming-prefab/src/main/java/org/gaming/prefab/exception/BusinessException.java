/**
 * 
 */
package org.gaming.prefab.exception;

import java.util.Arrays;

import org.gaming.prefab.ITipCause;

/**
 * @author YY
 *
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int stackTraceDepth = 10;

	private final ITipCause exceptionCause;
	private final Object[] params;
	
	
	public BusinessException(ITipCause exceptionCause, Object... params) {
		this.exceptionCause = exceptionCause;
		this.params = params;
		
		StackTraceElement[] stackTrace = this.getStackTrace();
		StackTraceElement[] mineStackTrace = new StackTraceElement[Math.min(stackTraceDepth, stackTrace.length)];
		System.arraycopy(stackTrace, 0, mineStackTrace, 0, mineStackTrace.length);
		this.setStackTrace(mineStackTrace);
	}

	public ITipCause getExceptionCause() {
		return exceptionCause;
	}

	public Object[] getParams() {
		return params;
	}
	
	@Override
	public String getMessage() {
		return exceptionCause.getDesc() + (params == null ? " " : Arrays.toString(params));
	}
}
