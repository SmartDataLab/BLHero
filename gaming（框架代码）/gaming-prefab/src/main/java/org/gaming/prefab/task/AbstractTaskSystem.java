/**
 * 
 */
package org.gaming.prefab.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.ruler.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class AbstractTaskSystem<T extends ITaskContainer> {

	private static Logger logger = LoggerFactory.getLogger(AbstractTaskSystem.class);

	// 不在这里做AbstractTaskSystem的注册，考虑到假如玩家有自己的任务系统、公会又有自己的任务系统，那么它们的任务注册应该是在不同的地方
	/**
	 * 监听所有的任务事件
	 * 
	 * @param taskEvent
	 */
	@Subscribe
	protected final void listen(ITaskEvent taskEvent) {
		List<T> taskContainers = taskContainers(taskEvent.getEntityId());
		for(T taskContainer : taskContainers) {
			// 发生了变化的任务数据列表
			List<Task> changedTasks = null;
			Map<String, List<TaskWrapper>> wrappersMap = this.getTasks(taskContainer, taskEvent.changes());
			for (TaskChange change : taskEvent.changes()) {
				List<TaskWrapper> wrappers = wrappersMap.get(change.getTaskType().identity());
				if (wrappers == null) {
					continue;
				}
				for (TaskWrapper wrapper : wrappers) {
					AbstractTaskEventProcessor processor = AbstractTaskEventProcessor.getProcessor(change.getTaskType());
					try {
						boolean updated = processor.handle(taskEvent.getEntityId(), wrapper.getTask(), change,
								wrapper.getTaskConfig());
						if (!updated) {
							continue;
						}
						if (changedTasks == null) {
							changedTasks = new ArrayList<>();
						}
						changedTasks.add(wrapper.getTask());
					} catch (Exception e) {
						logger.error("任务系统{}的任务{}处理发生异常{}", taskSystem().getDesc(), wrapper.getTask().getId(),
								e.getMessage());
					}
				}
			}
			if (changedTasks != null) {
				onChangeTask(taskContainer, taskEvent, changedTasks);
			}
		}
	}

	/**
	 * 是个什么任务系统
	 * 
	 * @return
	 */
	protected abstract ITaskSystem taskSystem();

	/**
	 * 获取该任务系统下的所有任务
	 * 
	 * @param ownerId 任务数据的持有者ID
	 * @return
	 */
	protected abstract List<T> taskContainers(long ownerId);

	/**
	 * 筛选该系统下某一任务类型的任务
	 * 
	 * @param entityId
	 * @param taskType
	 * @return
	 */
	private final Map<String, List<TaskWrapper>> getTasks(T taskContainer, TaskChange[] changes) {
		Map<String, List<TaskWrapper>> taskMap = new HashMap<>();
		for (Task task : taskContainer.getTasks()) {
			if (task.isDone() || task.isEmpty()) {
				continue;
			}
			ITaskConfig iTaskConfig = taskConfig(task.getId());
			for (TaskChange change : changes) {
				String taskIdentity = change.getTaskType().identity();
				if (!iTaskConfig.getIdentity().equals(taskIdentity)) {
					continue;
				}
				List<TaskWrapper> wrappers = taskMap.get(taskIdentity);
				if (wrappers == null) {
					wrappers = new ArrayList<>();
					taskMap.put(taskIdentity, wrappers);
				}
				wrappers.add(TaskWrapper.of(task, iTaskConfig));
				break;
			}
		}
		return taskMap;

	}

	/**
	 * 任务数据发生变化时的处理
	 * 
	 * @param rid
	 */
	protected abstract void onChangeTask(T taskContainer, ITaskEvent taskEvent, List<Task> changedTasks);

	/**
	 * 任务配置的缓存对象
	 * 
	 * @return
	 */
	protected abstract ITaskConfig taskConfig(int taskId);

}
