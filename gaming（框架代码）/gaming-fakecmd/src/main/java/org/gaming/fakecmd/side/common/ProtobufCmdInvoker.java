/**
 * 
 */
package org.gaming.fakecmd.side.common;

import java.lang.reflect.Method;

import com.google.protobuf.Message;

/**
 * @author YY
 *
 */
public class ProtobufCmdInvoker extends IntegerKeyCmdInvoker {
	
	private final Message message;
	
	public ProtobufCmdInvoker(int cmd, Object bean, Method method, Class<?> protobufClazz) throws Exception {
		super(cmd, bean, method);
		Method insMethod = protobufClazz.getDeclaredMethod("getDefaultInstance");
		this.message = (Message)insMethod.invoke(null);
	}
	
	public Message getMessage() {
		return message;
	}
}
