/**
 * 
 */
package com.xiugou.x1.backstage.module.clientlog.service;

import org.gaming.ruler.schedule.AbstractScheduler;
import org.gaming.ruler.schedule.IScheduleTask;
import org.gaming.ruler.schedule.LoopTask;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class ClientLogTimer extends AbstractScheduler {

	@Autowired
	private ClientLogService clientLogService;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("ClientLogTimer", DateTimeUtil.ONE_MINUTE_MILLIS, 5, () -> {
			clientLogService.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}

}
