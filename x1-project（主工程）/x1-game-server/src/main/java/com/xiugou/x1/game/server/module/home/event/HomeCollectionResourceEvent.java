package com.xiugou.x1.game.server.module.home.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/7/25
 * @apiNote 收集(特定)资源事件
 */
public class HomeCollectionResourceEvent implements ITaskEvent {
	private long pid;
	private int resourceId;
	private long num;

	public static HomeCollectionResourceEvent of(long pid, int resourceId, long num) {
		HomeCollectionResourceEvent event = new HomeCollectionResourceEvent();
		event.pid = pid;
		event.resourceId = resourceId;
		event.num = num;
		return event;
	}


	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.HOME_COLLECTION_RESOURCE, new int[] { resourceId }, num),
				TaskChange.of(TaskType.HOME_COLLECTION_NUM, new int[] { resourceId }, num) };
	}
}
