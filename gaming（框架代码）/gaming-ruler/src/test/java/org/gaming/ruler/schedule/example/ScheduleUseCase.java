/**
 * 
 */
package org.gaming.ruler.schedule.example;

import org.gaming.ruler.schedule.DisposableTask;
import org.gaming.ruler.schedule.LoopTask;
import org.gaming.ruler.schedule.ScheduleManager;

/**
 * @author YY
 *
 */
public class ScheduleUseCase {

	public static void main(String[] args) {
		ScheduleManager.setup(3);
		
		ScheduleManager.addTask(taskOf("CLAN11", System.currentTimeMillis() + 5000, 1));
		ScheduleManager.addTask(taskOf("CLAN12", System.currentTimeMillis() + 6000, 1));
		
		ScheduleManager.addTask(taskOf("CLAN21", System.currentTimeMillis() + 7000, 1));
		ScheduleManager.addTask(taskOf("CLAN22", System.currentTimeMillis() + 8000, 1));
		ScheduleManager.addTask(taskOf("CLAN23", System.currentTimeMillis() + 9000, 1));
		
		ScheduleManager.addTask(taskOf("CLAN31", System.currentTimeMillis() + 60000, 1));
		
		ScheduleManager.addTask(taskOf("CLAN41", System.currentTimeMillis() + 10000, 2));
		
		ScheduleManager.addTask(taskOf("CLAN51", System.currentTimeMillis() + 60000, 1));
		ScheduleManager.addTask(taskOf("CLAN52", System.currentTimeMillis() + 60000, 1));
		ScheduleManager.addTask(taskOf("CLAN53", System.currentTimeMillis() + 60000, 1));
		ScheduleManager.addTask(taskOf("CLAN54", System.currentTimeMillis() + 60000, 1));
		
		ScheduleManager.addTask(taskOf("CLAN61", System.currentTimeMillis(), 5000L, 1));
		
		ScheduleManager.print();
	}
	
	public static DisposableTask taskOf(String name, long time, int group) {
		return new TestDisposableTask(name, System.currentTimeMillis(), time, group);
	}
	
	public static LoopTask taskOf(String name, long time, long space, int group) {
		return new LoopTask(name, System.currentTimeMillis(), time, space, group, ()->{ScheduleManager.print();});
	}
}
