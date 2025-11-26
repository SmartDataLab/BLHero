/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.starting;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.schedule.ScheduleManager;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class ScheduleLifecycle implements Lifecycle {
	
	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MAX);
	}

	@Override
	public void start() throws Exception {
		ScheduleManager.setup(Runtime.getRuntime().availableProcessors());
	}
}
