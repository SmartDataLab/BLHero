/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.service;

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
public class FixTimer extends AbstractScheduler {

	@Autowired
	private FixDesignService fixDesignService;
	@Autowired
	private FixCodeService fixCodeService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task1 = new LoopTask("FixTimer-1", DateTimeUtil.ONE_SECOND_MILLIS * 10, 4, () -> {
			fixDesignService.runInSchedule();
		});
		IScheduleTask task2 = new LoopTask("FixTimer-2", DateTimeUtil.ONE_SECOND_MILLIS * 10, 4, () -> {
			fixCodeService.runInSchedule();
		});
		return new IScheduleTask[] { task1, task2 };
	}
}
