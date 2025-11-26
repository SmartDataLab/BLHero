/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

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
public class RechargerTimer extends AbstractScheduler {

	@Autowired
	private RechargeTodayService rechargeTodayService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("RechargerTimer", DateTimeUtil.ONE_HOUR_MILLIS, 2, () -> {
			rechargeTodayService.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}
}
