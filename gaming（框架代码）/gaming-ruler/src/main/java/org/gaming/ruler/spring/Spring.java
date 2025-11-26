/**
 * 
 */
package org.gaming.ruler.spring;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;


/**
 * @author YY
 *
 */
public class Spring {
	
	private static ApplicationContext context;
	
	public static void setContext(ApplicationContext context) {
		Spring.context = context;
	}
	
	public static <T> T getBean(Class<T> clz) {
		return context.getBean(clz);
	}
	
	public static <T> T getBean(String name, Class<T> clz) {
		return context.getBean(name, clz);
	}
	
	public static Collection<Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
		return context.getBeansWithAnnotation(annotationType).values();
	}
	
	/**
	 * 获取对象的实际类型
	 * @param bean
	 * @return
	 */
	public static Class<?> getBeanRealClass(Object bean) {
		Class<?> realClazz = null;
		if (AopUtils.isJdkDynamicProxy(bean)) {
			Object singletonTarget = AopProxyUtils.getSingletonTarget(bean);
			if (singletonTarget != null) {
				realClazz = singletonTarget.getClass();
			}
		} else if (AopUtils.isCglibProxy(bean)) {
			realClazz = bean.getClass().getSuperclass();
		} else {
			realClazz = bean.getClass();
		}
		return realClazz;
	}
}
