/**
 * 
 */
package org.gaming.prefab.exception;

import org.gaming.prefab.ITipCause;

/**
 * @author YY
 *
 */
public class Asserts {
	
	public static void isTrue(boolean expression, ITipCause tipCause, Object... params) {
		if(!expression) {
			throw new BusinessException(tipCause, params);
		}
	}
}
