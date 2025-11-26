/**
 * 
 */
package org.gaming.ruler.schedule.example;

import org.gaming.ruler.schedule.DisposableTask;
import org.gaming.ruler.schedule.ScheduleManager;

/**
 * @author YY
 *
 */
public final class TestDisposableTask extends DisposableTask {

	public TestDisposableTask(String name, long publishTime, long executeTime, int belongGroup) {
		super(name, publishTime, executeTime, belongGroup);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onRun() {
		System.out.println("DO DisposableTask " + getRawName());
		if("CLAN41".equals(getRawName())) {
			System.out.println("CLAN41 changeSystemTime");
			ScheduleManager.correctTime(System.currentTimeMillis(), System.currentTimeMillis() + 40000);
		}
	}

	@Override
	protected DisposableTask republishTask(long publishTime) {
		return new TestDisposableTask(getRawName(), publishTime, getExecuteTime(), getBelongGroup());
	}
}
