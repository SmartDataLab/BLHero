package org.gaming.ruler.lifecycle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 存活周期服务
 */
@Component
public class LifecycleSupport {
	
	private static Logger logger = LoggerFactory.getLogger(LifecycleSupport.class);
	
	private static final Comparator<Lifecycle> LIFECYCLE_COMPARATOR =
            Comparator.comparingInt(lifecycle -> lifecycle.getInfo().getOrder());

    private static List<Lifecycle> lifecycles = new ArrayList<>();
    
	public static void registerBean(Lifecycle bean) {
		lifecycles.add(bean);
	}
    
    /**
     * 顺序启动服务
     */
    public static void start() {
        Collections.sort(lifecycles, LIFECYCLE_COMPARATOR);
        
        int totalNumber = lifecycles.size();
        int i = 1;
        for (Lifecycle lifecycle : lifecycles) {
            long startTime = System.currentTimeMillis();
            String name = lifecycle.getInfo().getName();
            logger.info("({}/{}) [{}] service starting... {}", i, totalNumber, name, LocalDateTime.now());
            
            try {
            	lifecycle.start();
			} catch (Exception e) {
				logger.info(String.format("[%s] start throw exception", lifecycle.getInfo().getName()), e);
				throw new RuntimeException("Lifecycle start fail on " + lifecycle.getInfo().getName());
			}
            logger.info("({}/{}) [{}] service finished... {} use time:{}", i, totalNumber, name, LocalDateTime.now(),
                    (System.currentTimeMillis() - startTime));
            i++;
        }
    }

    /**
     * 逆序停止服务
     */
    public static void stop() {
    	Collections.reverse(lifecycles);
    	
        int totalNumber = lifecycles.size();
        int i = totalNumber;
        for (Lifecycle lifecycle : lifecycles) {
        	logger.info("({}/{})  [{}] service stopping...", i, totalNumber, lifecycle.getInfo().getName());
        	try {
        		lifecycle.stop();
			} catch (Exception e) {
				logger.error(String.format("[%s] stop throw exception", lifecycle.getInfo().getName()), e);
			}
            logger.info("({}/{})  [{}] service stopped...", i, totalNumber, lifecycle.getInfo().getName());
            i--;
        }
    }
}
