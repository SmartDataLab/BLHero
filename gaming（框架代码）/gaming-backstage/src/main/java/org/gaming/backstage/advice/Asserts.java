/**
 * 
 */
package org.gaming.backstage.advice;


/**
 * @author YY
 *
 */
public class Asserts {
	
	public static void isTrue(boolean expression, IResultTips resultTips, Object... params) {
		if(!expression) {
			throw new BusinessException(resultTips, params);
		}
	}
}
