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
public interface IScheduleTask extends Runnable {
	
	public static Logger logger = LoggerFactory.getLogger(IScheduleTask.class);
	
	/**
	 * 任务的名字
	 * @return
	 */
	String getName();
	/**
	 * 任务的执行时间
	 * @return
	 */
	long getExecuteTime();
	/**
	 * 任务的归属分组，比如某个系统下的任务都应该在同一条线程中执行，使用分组来区分是在哪个线程中
	 * @return
	 */
	int getBelongGroup();
}
