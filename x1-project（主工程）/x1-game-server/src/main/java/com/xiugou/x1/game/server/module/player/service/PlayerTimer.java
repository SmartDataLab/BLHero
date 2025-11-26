/**
 * 
 */
package com.xiugou.x1.game.server.module.player.service;

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
public class PlayerTimer extends AbstractScheduler {

	@Autowired
	private PlayerService playerService;

	@Override
	protected IScheduleTask[] tasks() {
		// TODO 统计在线人数，5分钟统计一次
		// TODO 统计注册人数，
		IScheduleTask task1 = new LoopTask("PlayerTimer-玩家数据统计", DateTimeUtil.ONE_MINUTE_MILLIS, 1, () -> {
			playerService.recordTimeLog();
			playerService.recordOnlineLog();
			playerService.recordeScatterLog();
		});
		IScheduleTask task2 = new LoopTask("PlayerTimer-后台数据上报", DateTimeUtil.ONE_SECOND_MILLIS * 30, 1, () -> {
			playerService.reportToBackstage();
			playerService.reportCreateTiming();
		});
		return new IScheduleTask[] { task1, task2 };
	}

}
