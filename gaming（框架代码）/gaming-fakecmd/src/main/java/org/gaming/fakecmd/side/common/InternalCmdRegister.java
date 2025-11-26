/**
 * 
 */
package org.gaming.fakecmd.side.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.cmd.AbstractCmdRegister;

/**
 * @author YY
 *
 */
public class InternalCmdRegister extends AbstractCmdRegister<String, StringKeyCmdInvoker> {

	public static InternalCmdRegister INS = new InternalCmdRegister();
	
	private InternalCmdRegister() {}
	
	@Override
	protected Class<? extends Annotation> annotationClass() {
		return InternalCmd.class;
	}

	@Override
	protected StringKeyCmdInvoker registerMethod(Object bean, Method method) {
		try {
			Class<?> messageClazz = method.getParameterTypes()[0];
			return new StringKeyCmdInvoker(messageClazz.getSimpleName(), bean, method);
		} catch (Exception e) {
			logger.error("类{}中的协议函数{}定义异常", bean.getClass().getSimpleName(), method.getName());
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Class<?>[] methodParamsType() {
		return new Class<?>[] { InternalCmdMessage.class };
	}
	
	public static interface InternalCmdMessage {
		
		void onException(Exception e);
	}
	
	public void handle(InternalCmdMessage message) {
		try {
			StringKeyCmdInvoker invoker = InternalCmdRegister.INS.getInvoker(message.getClass().getSimpleName());
			if(invoker == null) {
				logger.info("没有找到内部消息[{}]对应的处理函数", message.getClass().getSimpleName());
	            return;
			}
			invoker.getMethod().invoke(invoker.getBean(), message);
		} catch (Exception e) {
			message.onException(e);
		}
	}
	
}
