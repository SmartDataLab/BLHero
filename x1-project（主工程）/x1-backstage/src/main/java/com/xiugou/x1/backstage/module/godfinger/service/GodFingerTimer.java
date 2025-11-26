/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger.service;

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
public class GodFingerTimer extends AbstractScheduler {

	@Autowired
	private GodFingerService godFingerService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("GodFingerTimer", DateTimeUtil.ONE_MINUTE_MILLIS, 5, () -> {
			godFingerService.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}

}
