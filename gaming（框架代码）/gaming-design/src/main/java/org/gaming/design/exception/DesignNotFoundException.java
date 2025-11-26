/**
 * 
 */
package org.gaming.design.exception;

import java.util.Arrays;

/**
 * @author YY
 *
 */
public class DesignNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int stackTraceDepth = 10;

	private String method;
	private Object[] args;
	
	public DesignNotFoundException(String method, Object... args) {
		this.method = method;
		this.args = args;
		
		StackTraceElement[] stackTrace = this.getStackTrace();
		StackTraceElement[] mineStackTrace = new StackTraceElement[Math.min(stackTraceDepth, stackTrace.length)];
		System.arraycopy(stackTrace, 0, mineStackTrace, 0, mineStackTrace.length);
		this.setStackTrace(mineStackTrace);
	}

	@Override
	public String getMessage() {
		return method + " with args " + Arrays.toString(args) + " can not find config";
	}
}
