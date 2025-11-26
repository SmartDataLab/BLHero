/**
 * 
 */
package org.gaming.ruler.eventbus;

import java.lang.reflect.Method;

/**
 * @author YY
 * 事件处理器
 */
public class EventHandler {

	private final Object invoker;
	private final Method method;
	
	public EventHandler(Object invoker, Method method) {
		this.invoker = invoker;
		this.method = method;
	}

	public Object getInvoker() {
		return invoker;
	}

	public Method getMethod() {
		return method;
	}
}
