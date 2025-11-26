/**
 * 
 */
package com.xiugou.x1.game.server.module.player.difchannel;

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
public class DifChannelTimer extends AbstractScheduler {

	@Autowired
	private DifChannelManager difChannelManager;
	
	@Override
	protected IScheduleTask[] tasks() {
		IScheduleTask task = new LoopTask("DifChannelTimer-各渠道数据处理", DateTimeUtil.ONE_SECOND_MILLIS, 7, () -> {
			difChannelManager.runInSchedule();
		});
		return new IScheduleTask[] { task };
	}
	
}
