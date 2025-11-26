/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.service;

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
public class MainlineTimer extends AbstractScheduler {

	@Autowired
	private MainlineBattleProcessor mainlineBattleProcessor;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("MainlineTimer-清理主线战斗", DateTimeUtil.ONE_MINUTE_MILLIS * 10, 7, () -> {
			mainlineBattleProcessor.cleanBattle();
		});
		return new IScheduleTask[] { task };
	}
}
