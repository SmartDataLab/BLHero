package com.xiugou.x1.game.server.module.recruit.event;

import com.xiugou.x1.game.server.module.recruit.constant.RecruitType;
import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/6/26
 * @apiNote 招募次数事件
 */
public class RecruitNumEvent implements ITaskEvent {
	private long pid;
	//倍数
	private int multiple;
	
	private boolean costDiamond;

	public static RecruitNumEvent of(long pid, int multiple, boolean costDiamond) {
		RecruitNumEvent event = new RecruitNumEvent();
		event.pid = pid;
		event.multiple = multiple;
		event.costDiamond = costDiamond;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.RECRUIT_NUM),
				TaskChange.of(TaskType.NUMBER_OF_DRAWS, new int[] { RecruitType.HERO.getValue() }, 1) };
	}

	public long getPid() {
		return pid;
	}

	public int getMultiple() {
		return multiple;
	}

	public boolean isCostDiamond() {
		return costDiamond;
	}
}
