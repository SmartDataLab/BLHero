/**
 * 
 */
package org.gaming.fakecmd.side.common;

import java.lang.reflect.Method;

import org.gaming.fakecmd.cmd.AbstractCmdInvoker;

/**
 * @author YY
 *
 */
public class IntegerKeyCmdInvoker extends AbstractCmdInvoker<Integer> {

	public IntegerKeyCmdInvoker(int cmd, Object bean, Method method) {
		super(cmd, bean, method);
	}
}
