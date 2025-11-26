/**
 * 
 */
package org.gaming.ruler.schedule;

/**
 * @author YY
 *
 */
public abstract class AbstractScheduler {

	public AbstractScheduler() {
		ScheduleManager.addScheduler(this);
	}
	
	protected abstract IScheduleTask[] tasks();
}
