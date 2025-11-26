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
public class StringKeyCmdInvoker extends AbstractCmdInvoker<String> {

	public StringKeyCmdInvoker(String cmd, Object bean, Method method) {
		super(cmd, bean, method);
	}
}
