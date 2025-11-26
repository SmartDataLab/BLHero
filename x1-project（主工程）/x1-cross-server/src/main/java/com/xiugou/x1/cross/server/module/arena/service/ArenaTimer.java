/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.service;

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
public class ArenaTimer extends AbstractScheduler {

	@Autowired
	private ArenaSystemService arenaSystemService;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task1 = new LoopTask("ArenaTimer", DateTimeUtil.ONE_SECOND_MILLIS * 10, 1, () -> {
			arenaSystemService.runInSchedule();
		});
		return new IScheduleTask[] {task1};
	}

}
