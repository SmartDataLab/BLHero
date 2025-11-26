/**
 * 
 */
package org.gaming.prefab.task;

/**
 * @author YY
 *
 */
public interface ITaskEvent {
	/**
	 * 实体ID，比如玩家ID、公会ID等
	 * @return
	 */
	long getEntityId();
	/**
	 * 当前发生的任务变化
	 * @return
	 */
	TaskChange[] changes();
}
