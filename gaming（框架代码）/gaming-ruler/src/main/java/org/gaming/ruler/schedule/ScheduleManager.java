/**
 * 
 */
package org.gaming.ruler.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.gaming.ruler.thread.NamesThreadFactory;

/**
 * @author YY
 *
 */
public class ScheduleManager {

	private static ScheduledExecutorService[] executors;
	
	protected static Queue<IScheduleTask> taskQueue = new ConcurrentLinkedQueue<>();
	
	private static Queue<AbstractScheduler> schedulers = new ConcurrentLinkedQueue<>();
	
	public static void setup(int threadCount) {
		if(threadCount <= 0) {
			threadCount = 1;
		}
		NamesThreadFactory threadFactory = new NamesThreadFactory("SCHEDULE");
		executors = new ScheduledExecutorService[threadCount];
		for(int i = 0; i < executors.length; i++) {
			executors[i] = Executors.newSingleThreadScheduledExecutor(threadFactory);
		}
		for(AbstractScheduler scheduler : schedulers) {
			for(IScheduleTask task : scheduler.tasks()) {
				addTask(task);
			}
		}
	}
	
	public static void addScheduler(AbstractScheduler schedule) {
		schedulers.add(schedule);
	}
	
	public static void addTask(IScheduleTask task) {
		if(executors == null) {
			throw new RuntimeException("定时管理器还没有调用setup函数");
		}
		taskQueue.add(task);
		
		ScheduledExecutorService executorService = executors[task.getBelongGroup() % executors.length];
		
		if(task instanceof DisposableTask) {
			DisposableTask disposableTask = (DisposableTask) task;
			long delay = disposableTask.getExecuteTime() - disposableTask.getPublishTime();
			executorService.schedule(disposableTask, delay, TimeUnit.MILLISECONDS);
		} else if(task instanceof LoopTask) {
			LoopTask loopTask = (LoopTask) task;
			long initialDelay = loopTask.getExecuteTime() - loopTask.getPublishTime();
			long delay = loopTask.getSpaceTime();
			executorService.scheduleWithFixedDelay(loopTask, initialDelay, delay, TimeUnit.MILLISECONDS);
		} else {
		}
	}
	
	/**
	 * 校正时间
	 * 在修改了系统时间之后，需要对一次性的定时任务进行重新发布
	 * @param oldCurrTime
	 * @param newCurrTime
	 */
	public static void correctTime(long oldCurrTime, long newCurrTime) {
		List<DisposableTask> taskList = new ArrayList<>();
		for(IScheduleTask task : taskQueue) {
			if(task instanceof DisposableTask) {
				taskList.add((DisposableTask) task);
			}
		}
		for(DisposableTask task : taskList) {
			DisposableTask newTask = task.republish(newCurrTime);
			if(newTask != null) {
				System.out.println("newTask " + newTask.getName() + " " + newTask.getExecuteTime());
				//重新添加新的任务
				addTask(newTask);
				//从队列中移除老的任务
				taskQueue.remove(task);
			}
		}
	}
	
	/**
	 * 丢弃所有一次性的任务
	 */
	public static void discardDisposableTasks() {
		for(IScheduleTask task : taskQueue) {
			if(task instanceof DisposableTask) {
				DisposableTask disposableTask = (DisposableTask) task;
				boolean success = disposableTask.discard();
				if(success) {
					//TODO 放弃任务成功
				}
			}
		}
	}
	
	public static void print() {
		SchedulePrinter.print(executors.length, taskQueue);
	}
}
