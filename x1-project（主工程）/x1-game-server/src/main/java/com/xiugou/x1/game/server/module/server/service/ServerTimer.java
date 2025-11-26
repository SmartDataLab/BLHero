/**
 * 
 */
package com.xiugou.x1.game.server.module.server.service;

import org.gaming.ruler.schedule.AbstractScheduler;
import org.gaming.ruler.schedule.IScheduleTask;
import org.gaming.ruler.schedule.LoopTask;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.server.model.ServerInfo;

/**
 * @author YY
 *
 */
@Component
public class ServerTimer extends AbstractScheduler {

	@Autowired
	private ServerInfoService serverInfoService;

	@Override
	protected IScheduleTask[] tasks() {
		ServerInfo serverInfo = serverInfoService.getMain();
		
		IScheduleTask task1 = new LoopTask("ServerTimer-开服", DateTimeUtil.ONE_SECOND_MILLIS, 0, () -> {
			serverInfoService.runForOpen();
		});
		
		IScheduleTask task2 = new LoopTask("ServerTimer-游戏汇总统计", DateTimeUtil.ONE_SECOND_MILLIS * 10, 0, () -> {
			serverInfoService.runForResume();
		});
		
		IScheduleTask task3 = new LoopTask("ServerTimer-游戏每日重置", DateTimeUtil.ONE_SECOND_MILLIS, 0, () -> {
			serverInfoService.runForDailyReset();
		});
		
		IScheduleTask task4 = new LoopTask("ServerTimer-服务器心跳", DateTimeUtil.ONE_SECOND_MILLIS, 0, () -> {
			serverInfoService.runForHeartBeat();
		});
		
		if (serverInfo.isOpened()) {
			//已经开服后，再次启动服务器时将不再注册开服定时器
			return new IScheduleTask[] { task2, task3, task4 };
		} else {
			return new IScheduleTask[] { task1, task2, task3, task4 };
		}
	}
}
