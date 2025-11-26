package com.xiugou.x1.game.server.module.recruit.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.recruit.constant.RefreshType;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 招募刷新次数事件
 */
public class RecruitRefreshEvent implements ITaskEvent {
	private long pid;
	private RefreshType refreshType;

	public static RecruitRefreshEvent of(long pid, RefreshType refreshType) {
		RecruitRefreshEvent event = new RecruitRefreshEvent();
		event.pid = pid;
		event.refreshType = refreshType;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		if (refreshType.isCountHand()) {
			return new TaskChange[] { TaskChange.of(TaskType.RECRUIT_REFRESH) };
		} else {
			return new TaskChange[0];
		}
	}

	public long getPid() {
		return pid;
	}

	public RefreshType getRefreshType() {
		return refreshType;
	}
}
