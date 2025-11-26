/**
 * 
 */
package com.xiugou.x1.game.server.module.purgatory.service;

import org.gaming.ruler.schedule.AbstractScheduler;
import org.gaming.ruler.schedule.IScheduleTask;
import org.gaming.ruler.schedule.LoopTask;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author YY
 *
 */
//@Component
public class PurgatoryTimer extends AbstractScheduler {

	@Autowired
	private PurgatorySystemService purgatorySystemService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("PurgatoryTimer-炼狱轮回结算", DateTimeUtil.ONE_SECOND_MILLIS * 10, 6, () -> {
			purgatorySystemService.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}
}
