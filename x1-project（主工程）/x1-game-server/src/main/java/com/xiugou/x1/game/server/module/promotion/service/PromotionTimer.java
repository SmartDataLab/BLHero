/**
 * 
 */
package com.xiugou.x1.game.server.module.promotion.service;

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
public class PromotionTimer extends AbstractScheduler {

	@Autowired
	private PromotionControlService promotionControlService;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("PromotionTimer-活动流程控制", DateTimeUtil.ONE_SECOND_MILLIS, 3, () -> {
			promotionControlService.runInSchedule();
		});
		return new IScheduleTask[] {task};
	}

}
