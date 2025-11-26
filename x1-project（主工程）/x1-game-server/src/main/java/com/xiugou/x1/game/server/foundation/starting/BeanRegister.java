/**
 * 
 */
package com.xiugou.x1.game.server.foundation.starting;

import org.gaming.fakecmd.side.game.GameCmdManager;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Component
public class BeanRegister implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Lifecycle) {
			// 生命周期服务注册
			LifecycleSupport.registerBean((Lifecycle) bean);
		}
		Controller controller = bean.getClass().getAnnotation(Controller.class);
		if (controller != null) {
			// 协议处理注册
			GameCmdManager.registerBean(bean);
		}
		Service service = bean.getClass().getAnnotation(Service.class);
		Component component = bean.getClass().getAnnotation(Component.class);
		if(service != null || component != null) {
			EventBus.register(bean);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
