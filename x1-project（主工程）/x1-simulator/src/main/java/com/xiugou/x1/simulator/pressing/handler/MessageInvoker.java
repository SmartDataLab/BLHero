/**
 * 
 */
package com.xiugou.x1.simulator.pressing.handler;

import java.lang.reflect.Method;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

/**
 * @author YY
 *
 */
public class MessageInvoker {
	private int protocolId;
	private Object invoker;
	private Method method;
	private Class<? extends GeneratedMessageV3> protocolClazz;
	private Message message;
	
	public MessageInvoker(int protocolId, Object invoker, Method method,
			Class<? extends GeneratedMessageV3> protocolClazz) {
		this.protocolId = protocolId;
		this.invoker = invoker;
		this.method = method;
		this.protocolClazz = protocolClazz;
		try {
			Method insMethod = protocolClazz.getDeclaredMethod("getDefaultInstance");
			this.message = (Message)insMethod.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getProtocolId() {
		return protocolId;
	}

	public Object getInvoker() {
		return invoker;
	}

	public Method getMethod() {
		return method;
	}

	public Class<? extends GeneratedMessageV3> getProtocolClazz() {
		return protocolClazz;
	}

	public Message getMessage() {
		return message;
	}
}
