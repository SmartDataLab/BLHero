/**
 * 
 */
package org.gaming.prefab.task;

import java.util.HashMap;
import java.util.Map;

import org.gaming.ruler.spring.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class AbstractTaskEventProcessor {

	private static Logger logger = LoggerFactory.getLogger(AbstractTaskEventProcessor.class);
	/**
	 * 任务事件处理器集合
	 */
	private static final Map<ITaskType, AbstractTaskEventProcessor> TASK_PROCESSORS = new HashMap<>();
	public static void register(ITaskType taskType, Class<? extends AbstractTaskEventProcessor> processorClazz) {
		if(processorClazz == null) {
			logger.warn(taskType.identity() + "未定义处理器");
			return;
		}
		AbstractTaskEventProcessor processor = Spring.getBean(processorClazz);
		if(processor == null) {
			logger.error("未找到任务类型{}的处理器", taskType.identity());
			return;
		}
		TASK_PROCESSORS.put(taskType, processor);
	}
	public static AbstractTaskEventProcessor getProcessor(ITaskType taskType) {
		return TASK_PROCESSORS.get(taskType);
	}
	
	
	
	/**
	 * 
	 * @param task
	 * @param requirement
	 * @param taskConfig
	 * @return true需要更新数据
	 */
	public final boolean handle(long entityId, Task task, TaskChange change, ITaskConfig taskConfig) {
		if(task.isDone() || task.isEmpty()) {
			return false;
		}
		return handling(entityId, task, change, taskConfig);
	}
	/**
	 * 
	 * @param task
	 * @param requirement
	 * @param taskConfig
	 * @return true需要更新数据
	 */
	protected abstract boolean handling(long entityId, Task task, TaskChange change, ITaskConfig taskConfig);
}
