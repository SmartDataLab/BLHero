package com.xiugou.x1.game.server.module.dailyWeekly.event;

/**
 * @author yh
 * @date 2023/9/4
 * @apiNote
 */
public class DailyWeeklyPointEvent {
	private long pid;

	private long plusPoint;

	public static DailyWeeklyPointEvent of(long pid, long plusPoint) {
		DailyWeeklyPointEvent event = new DailyWeeklyPointEvent();
		event.pid = pid;
		event.plusPoint = plusPoint;
		return event;
	}

	public long getPid() {
		return pid;
	}

	public long getPlusPoint() {
		return plusPoint;
	}

}
