/**
 * 
 */
package org.gaming.fakecmd.cmd;

import java.lang.reflect.Method;

/**
 * @author YY
 *
 */
public abstract class AbstractCmdInvoker<K> {
	/**
	 * 命令标识
	 */
	private final K cmd;
	/**
	 * 调用方法的实例，即对应函数所在类的实例
	 */
	private final Object bean;
	/**
	 * 处理函数
	 */
	private final Method method;
	
	public AbstractCmdInvoker(K cmd, Object bean, Method method) {
		this.cmd = cmd;
		this.bean = bean;
		this.method = method;
	}
	
	public K getCmd() {
		return cmd;
	}
	
	public Object getBean() {
		return bean;
	}
	
	public Method getMethod() {
		return method;
	}
	
}
