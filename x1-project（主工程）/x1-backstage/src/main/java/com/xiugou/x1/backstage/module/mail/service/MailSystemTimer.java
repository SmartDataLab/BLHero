/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.service;

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
public class MailSystemTimer extends AbstractScheduler {

	@Autowired
	private MailSystemService mailSystemService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task1 = new LoopTask("MailSystemTimer", DateTimeUtil.ONE_SECOND_MILLIS, 3, () -> {
			mailSystemService.runInSchedule();
		});
		return new IScheduleTask[] { task1 };
	}
}
