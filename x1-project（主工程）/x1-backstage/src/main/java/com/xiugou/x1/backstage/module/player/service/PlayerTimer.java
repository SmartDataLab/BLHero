/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

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
	private PlayerRemainService playerRemainService;
	@Autowired
	private PlayerPayRemainService playerPayRemainService;
	@Autowired
	private PlayerLTVService playerLTVService;

	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("PlayerTimer", DateTimeUtil.ONE_SECOND_MILLIS * 10, 1, () -> {
			try {
				playerRemainService.runInSchedule();
			} catch (Exception e) {
			}
			try {
				playerLTVService.runInSchedule();
			} catch (Exception e) {
			}
			try {
				playerPayRemainService.runInSchedule();
			} catch (Exception e) {
			}
		});
		return new IScheduleTask[] { task };
	}
}
