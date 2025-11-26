/**
 * 
 */
package org.gaming.ruler.schedule;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YY
 * 一次性的任务
 */
public abstract class DisposableTask implements IScheduleTask {
	//未执行
	public static final int WAITING = 0;
	//已执行
	public static final int DONE = 1;
	//已丢弃
	public static final int DISCARDED = 2;
	
	
	//任务的名字
	protected final String name;
	//任务的发布时间
	protected final long publishTime;
	//任务的预期执行时间
	protected final long executeTime;
	//所属的线程分组
	protected final int belongGroup;
	
	//0未执行，1已执行，2已丢弃
	private final AtomicInteger status = new AtomicInteger(WAITING);
	
	public DisposableTask(String name, long publishTime, long executeTime, int belongGroup) {
		this.name = name;
		this.publishTime = publishTime;
		this.executeTime = executeTime;
		this.belongGroup = belongGroup;
	}

	public String getName() {
		int s = status.get();
		if(s == DONE) {
			return name + "(DONE)";
		} else if(s == DISCARDED) {
			return name + "(DISCARDED)";
		} else {
			return name + "(WAITING)";
		}
	}
	
	public String getRawName() {
		return name;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public int getBelongGroup() {
		return belongGroup;
	}

	@Override
	public final void run() {
		try {
			if(status.compareAndSet(WAITING, DONE)) {
				onRun();
			} else {
				logger.info("任务【{}】已被丢弃", name);
				System.out.println("任务【" + name + "】已被丢弃");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			onFinish();
		}
	}
	
	/**
	 * 子类实现的逻辑函数
	 */
	public abstract void onRun();
	
	private final void onFinish() {
		ScheduleManager.taskQueue.remove(this);
	}
	
	/**
	 * 任务重新发布
	 * 当无法丢弃当前任务时返回null，在任务已经执行后则无法丢弃
	 * @return
	 */
	public final DisposableTask republish(long publishTime) {
		if(!status.compareAndSet(WAITING, DISCARDED)) {
			//正在执行或者已经执行的任务不能重新发布
			return null;
		}
		DisposableTask task = republishTask(publishTime);
		return task;
	}
	
	/**
	 * 子类实现的重新生成任务的函数
	 * 因为某些任务中会有自己的上下文信息（任务内部记录的状态属性），因此重新发布任务的职责需要由子类来实现
	 */
	protected abstract DisposableTask republishTask(long publishTime);
	
	public boolean discard() {
		return status.compareAndSet(WAITING, DISCARDED);
	}
}
