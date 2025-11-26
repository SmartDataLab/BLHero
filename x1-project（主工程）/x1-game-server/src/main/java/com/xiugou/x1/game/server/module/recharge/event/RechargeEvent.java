/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.event;

import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
public class RechargeEvent implements ITaskEvent {
	private long pid;
	private Recharge recharge;
	
	public static RechargeEvent of(long pid, Recharge recharge) {
		RechargeEvent event = new RechargeEvent();
		event.pid = pid;
		event.recharge = recharge;
		return event;
	}
	
	@Override
	public long getEntityId() {
		return pid;
	}
	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.ACCUMULATE_RECHARGE, getPayMoney()) };
	}

	public long getPid() {
		return pid;
	}

	public Recharge getRecharge() {
		return recharge;
	}
	
	public long getPayMoney() {
		return recharge.isVirtual() ? recharge.getPrePayMoney() : recharge.getPayMoney();
	}
}
