/**
 * 
 */
package org.gaming.fakecmd.side.game;

import java.lang.reflect.Method;

import org.gaming.fakecmd.side.common.ProtobufCmdInvoker;

/**
 * @author YY
 *
 */
public class PlayerCmdInvoker extends ProtobufCmdInvoker {
	/**
	 * 是否需要登录才能调用
	 */
	private final boolean needLogin;
	/**
	 * 转发至
	 */
	private final String forwardTo;

	public PlayerCmdInvoker(int cmd, boolean needLogin, String forwardTo, Object bean, Method method, Class<?> protobufClazz)
			throws Exception {
		super(cmd, bean, method, protobufClazz);
		this.needLogin = needLogin;
		this.forwardTo = forwardTo;
	}

	// get...

	public boolean isNeedLogin() {
		return needLogin;
	}

	public String getForwardTo() {
		return forwardTo;
	}
}
