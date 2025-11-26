/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.service;

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
public class DotDataTimer extends AbstractScheduler {

	@Autowired
	private LoginDotDataService loginDotDataService;
	@Autowired
	private LoginDotDataStatisticService loginDotDataStatisticService;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task1 = new LoopTask("DotDataTimer-保存登录打点数据", DateTimeUtil.ONE_MINUTE_MILLIS * 1, 5, () -> {
			loginDotDataService.runForSaveDot();
		});
		IScheduleTask task2 = new LoopTask("DotDataTimer-统计登录打点数据", DateTimeUtil.ONE_HOUR_MILLIS, 5, () -> {
			loginDotDataStatisticService.runForSaveStatistic();
		});
//		IScheduleTask task3 = new LoopTask("DotDataTimer-统计任务打点数据", DateTimeUtil.ONE_HOUR_MILLIS, 5, () -> {
//			
//		});
		
		return new IScheduleTask[] {task1, task2};
	}

	
}
