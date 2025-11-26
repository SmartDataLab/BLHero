/**
 * 
 */
package com.xiugou.x1.backstage.foundation.starting;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.schedule.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class ScheduleLifecycle implements Lifecycle {
	
	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MAX);
	}

	@Override
	public void start() throws Exception {
		//只在主后台跑定时器
		if(applicationSettings.isServerBackstageMain()) {
			ScheduleManager.setup(Runtime.getRuntime().availableProcessors());
		}
	}
}
