/**
 * 
 */
package com.xiugou.x1.backstage.foundation.starting;

import org.gaming.ruler.lifecycle.LifecycleSupport;
import org.gaming.ruler.spring.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author YY
 *
 */
public class ProjectBooter implements ApplicationListener<ApplicationContextEvent> {

	private static Logger logger = LoggerFactory.getLogger(ProjectBooter.class);
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if(event instanceof ContextRefreshedEvent) {
			Spring.setContext(event.getApplicationContext());

			logger.info("执行服务器启动逻辑");
			LifecycleSupport.start();
		} else if(event instanceof ContextClosedEvent) {
			logger.info("执行服务器关停逻辑");
			LifecycleSupport.stop();
		}
	}

}
