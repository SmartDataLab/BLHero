/**
 * 
 */
package org.gaming.prefab.task;

import java.util.List;

/**
 * @author YY
 * 任务的容器，比如玩家的日常周常数据实体
 */
public interface ITaskContainer {
	long getOwnerId();
	List<Task> getTasks();
}
