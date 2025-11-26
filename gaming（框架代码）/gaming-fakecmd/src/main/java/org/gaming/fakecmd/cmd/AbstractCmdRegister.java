/**
 * 
 */
package org.gaming.fakecmd.cmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author YY
 *
 */
public abstract class AbstractCmdRegister<K, I extends AbstractCmdInvoker<K>> {

	protected static Logger logger = LoggerFactory.getLogger(AbstractCmdRegister.class);
	
	private final Map<K, I> invokers = new HashMap<>();
	
	public final void register(Object bean) {
		for(Method method : bean.getClass().getDeclaredMethods()) {
			try {
				Annotation annotationClass = method.getAnnotation(annotationClass());
				if(annotationClass == null) {
					continue;
				}
				if (method.getParameterTypes().length != methodParamsType().length) {
					String errorMsg = String.format("通过%s注册的处理函数%s.%s未符合有且只有%s个参数的要求",
							annotationClass().getSimpleName(), bean.getClass().getSimpleName(), method.getName(),
							methodParamsType().length);
					throw new IllegalArgumentException(errorMsg);
				}
				for(int i = 0; i < methodParamsType().length; i++) {
					Class<?> requireClazz = methodParamsType()[i];
					Class<?> actualClazz = method.getParameterTypes()[i];
					if(!requireClazz.isAssignableFrom(actualClazz)) {
						String errorMsg = String.format("通过%s注册的处理函数%s.%s第%s个参数的类型必须是%s的子类或实现类",
								annotationClass().getSimpleName(), bean.getClass().getSimpleName(), method.getName(),
								(i + 1), requireClazz.getSimpleName());
						throw new IllegalArgumentException(errorMsg);
					}
				}
				I invoker = registerMethod(bean, method);
				if(invokers.containsKey(invoker.getCmd())) {
					I duplicateInvoker = invokers.get(invoker.getCmd());
					logger.error("{}指令{}重复注册，来源1{}.{}，来源2{}.{}", annotationClass.getClass().getSimpleName(),
							invoker.getCmd(), duplicateInvoker.getBean().getClass().getSimpleName(),
							duplicateInvoker.getMethod().getName(), invoker.getBean().getClass().getSimpleName(),
							invoker.getMethod().getName());
					String errMsg = String.format("重复注册的%s指令%s", annotationClass.getClass().getSimpleName(),
							invoker.getCmd());
					throw new RuntimeException(errMsg);
				}
				invokers.put(invoker.getCmd(), invoker);
			} catch (Exception e) {
				logger.error("类{}中的指令函数{}定义异常", bean.getClass().getSimpleName(), method.getName());
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 关注的注解类型
	 * @return
	 */
	protected abstract Class<? extends Annotation> annotationClass();
	/**
	 * 实例函数的参数类型
	 * @return
	 */
	protected abstract Class<?>[] methodParamsType();
	/**
	 * 注册实例的方法
	 * @param bean
	 * @param method
	 * @return
	 */
	protected abstract I registerMethod(Object bean, Method method);
	
	public final I getInvoker(K cmd) {
		return invokers.get(cmd);
	}
	
	public void printAllInvokers() {
		for(I invoker : invokers.values()) {
			String msgClassName = invoker.getBean().getClass().getSimpleName() + "." + invoker.getMethod().getName();
			System.out.println(invoker.getCmd() + "\t" + msgClassName);
		}
	}
}
