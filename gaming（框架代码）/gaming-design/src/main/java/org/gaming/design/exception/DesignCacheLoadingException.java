/**
 * 
 */
package org.gaming.design.exception;

/**
 * @author YY
 *
 */
public class DesignCacheLoadingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DesignCacheLoadingException(String fileName) {
		super(fileName);
	}
}
