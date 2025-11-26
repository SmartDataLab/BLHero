/**
 * 
 */
package org.gaming.ruler.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public final class LoopTask implements IScheduleTask {
	
	private static Logger logger = LoggerFactory.getLogger(LoopTask.class);
	
	//任务的名字
	protected final String name;
	//任务的发布时间
	protected final long publishTime;
	//任务的预期执行时间
	protected final long executeTime;
	//任务执行间隔时间
	protected final long spaceTime;
	//所属的分组
	protected final int belongGroup;
	
	protected final LoopTaskFunction function;
	
	public LoopTask(String name, long spaceMillis, int belongGroup, LoopTaskFunction function) {
		this(name, System.currentTimeMillis(), System.currentTimeMillis(), spaceMillis, belongGroup, function);
	}
	
	public LoopTask(String name, long publishTime, long executeTime, long spaceMillis, int belongGroup, LoopTaskFunction function) {
		this.name = name;
		this.publishTime = publishTime;
		this.executeTime = executeTime;
		this.spaceTime = spaceMillis;
		this.belongGroup = belongGroup;
		this.function = function;
	}

	public String getName() {
		return name + "(LOOP)";
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
	
	public long getSpaceTime() {
		return spaceTime;
	}
	
	@Override
	public final void run() {
		try {
			function.apply();
		} catch (Exception e) {
			logger.error("loop task cause exception", e);
		}
	}
	
	public static interface LoopTaskFunction {
    	void apply();
    }
}
