package com.xiugou.x1.game.server.module.shop.event;

import com.xiugou.x1.game.server.module.task.TaskType;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

/**
 * @author yh
 * @date 2023/8/15
 * @apiNote 兑换商店兑换次数事件
 */
public class ExchangeShopEvent implements ITaskEvent {
	private long pid;
	private int num;
	private boolean watchAdv;

	public static ExchangeShopEvent of(long pid, int num, boolean watchAdv) {
		ExchangeShopEvent event = new ExchangeShopEvent();
		event.pid = pid;
		event.num = num;
		event.watchAdv = watchAdv;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}

	@Override
	public TaskChange[] changes() {
		if(watchAdv) {
			return new TaskChange[] { TaskChange.of(TaskType.EXCHANGE_SHOP, num), TaskChange.of(TaskType.WATCH_ADV) };
		} else {
			return new TaskChange[] { TaskChange.of(TaskType.EXCHANGE_SHOP, num) };
		}
	}
}
