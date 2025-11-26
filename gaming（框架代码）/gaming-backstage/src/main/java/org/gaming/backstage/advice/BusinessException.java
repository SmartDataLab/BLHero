/**
 * 
 */
package org.gaming.backstage.advice;

/**
 * @author YY
 *
 */
public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IResultTips resultTips;
	private Object[] params;
	
	public BusinessException(IResultTips resultTips, Object... params) {
		this.resultTips = resultTips;
		this.params = params;
	}
	
	public int getCode() {
		return resultTips.getCode();
	}
	
	public String getMessage() {
		if(params == null) {
			return resultTips.getMessage();
		} else {
			return String.format(resultTips.getMessage(), params);
		}
	}

	public Object[] getParams() {
		return params;
	}
}
