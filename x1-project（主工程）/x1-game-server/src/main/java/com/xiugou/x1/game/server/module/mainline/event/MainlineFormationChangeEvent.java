/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.event;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.TaskChange;

import com.xiugou.x1.game.server.module.task.TaskType;

/**
 * @author YY
 *
 */
public class MainlineFormationChangeEvent implements ITaskEvent {
	private long pid;
	//上阵人数
	private int num;
	//主线编队战斗力
	private long fighting;
	//队长的英雄标识
	private int leader;
	
	private IGameCause gameCause;
	
	public static MainlineFormationChangeEvent of(long pid, int num, long fighting, int leader, IGameCause gameCause) {
		MainlineFormationChangeEvent event = new MainlineFormationChangeEvent();
		event.pid = pid;
		event.num = num;
		event.fighting = fighting;
		event.leader = leader;
		event.gameCause = gameCause;
		return event;
	}

	@Override
	public long getEntityId() {
		return pid;
	}
	
	@Override
	public TaskChange[] changes() {
		return new TaskChange[] { TaskChange.of(TaskType.FORMATION_NUM, num) };
	}

	public long getPid() {
		return pid;
	}

	public int getNum() {
		return num;
	}

	public long getFighting() {
		return fighting;
	}

	public IGameCause getGameCause() {
		return gameCause;
	}

	public int getLeader() {
		return leader;
	}
}
