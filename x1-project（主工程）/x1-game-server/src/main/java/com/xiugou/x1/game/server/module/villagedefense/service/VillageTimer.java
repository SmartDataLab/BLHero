package com.xiugou.x1.game.server.module.villagedefense.service;

import org.gaming.ruler.schedule.AbstractScheduler;
import org.gaming.ruler.schedule.IScheduleTask;
import org.gaming.ruler.schedule.LoopTask;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yh
 * @date 2023/8/18
 * @apiNote
 */
//@Component
public class VillageTimer extends AbstractScheduler {
	@Autowired
	private VillageSystemService villageSystemService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("VillageTimer-村庄防御结算", DateTimeUtil.ONE_SECOND_MILLIS * 10, 5, () -> {
			villageSystemService.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}
}
