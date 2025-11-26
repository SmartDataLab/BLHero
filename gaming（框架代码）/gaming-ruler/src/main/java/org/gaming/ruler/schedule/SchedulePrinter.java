/**
 * 
 */
package org.gaming.ruler.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.tool.DateTimeUtil;

/**
 * @author YY
 *
 */
public class SchedulePrinter {

	public static final int CELL_WIDTH = 30;
	
	public static void print(int executorNum, Collection<IScheduleTask> tasks) {
		
		SchedulePrinter[] printers = new SchedulePrinter[executorNum];
		for(int i = 0; i < printers.length; i++) {
			printers[i] = new SchedulePrinter();
			printers[i].setIndex(i);
		}
		for(IScheduleTask task : tasks) {
			printers[task.getBelongGroup() % executorNum].addTask(task);
		}
		Set<Integer> allTimes = new HashSet<>();
		for(SchedulePrinter printer : printers) {
			allTimes.addAll(printer.tickMap.keySet());
		}
		List<Integer> allTimeList = new ArrayList<>(allTimes);
		Collections.sort(allTimeList);
		
		StringBuilder builder = new StringBuilder("  ");
		for(int time : allTimeList) {
			builder.append(format(DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, time * 1000L) + ""));
		}
		System.out.println(builder.toString());
		for(SchedulePrinter printer : printers) {
			printer.print(allTimeList, 0);
		}
	}
	
	
	private int index;
	//打印的粒度只需要到秒就可以
	private Map<Integer, List<String>> tickMap = new HashMap<>();
	
	public void addTask(IScheduleTask task) {
		int secondTime = (int) (task.getExecuteTime() / 1000);
		List<String> tick = tickMap.get(secondTime);
		if(tick == null) {
			tick = new ArrayList<>();
			tickMap.put(secondTime, tick);
		}
		tick.add(task.getName());
	}
	
	/**
	 * 
	 * @param times
	 * @param futureTime 需要打印到多久之后的时间，还没有实现
	 */
	public void print(List<Integer> times, long futureTime) {
		int maxTaskCount = 1;
		for(List<String> tick : tickMap.values()) {
			if(tick.size() > maxTaskCount) {
				maxTaskCount = tick.size();
			}
		}
		StringBuilder[] builders = new StringBuilder[maxTaskCount];
		for(int i = 0; i < maxTaskCount; i++) {
			builders[i] = new StringBuilder();
		}
		
		allAppend(builders, index + " ");
		
		for(int time : times) {
			List<String> tick = tickMap.get(time);
			if(tick == null) {
				allAppend(builders, space());
			} else {
				for(int i = 0; i < maxTaskCount; i++) {
					if(i < tick.size()) {
						builders[i].append(format(tick.get(i)));
					} else {
						builders[i].append(space());
					}
				}
			}
		}
		for(StringBuilder builder : builders) {
			System.out.println(builder.toString());
		}
	}
	
	/**
	 * 生成空格
	 * @return
	 */
	private static String space() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < CELL_WIDTH; i++) {
			builder.append(" ");
		}
		return builder.toString();
	}
	
	/**
	 * 填充空格至指定长度
	 * @return
	 */
	private static String format(String str) {
		int maxLength = CELL_WIDTH - 2;
		if(str.length() >= maxLength) {
			return str.substring(0, maxLength) + "  ";
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(str);
			for(int i = 0; i < CELL_WIDTH - str.length(); i++) {
				builder.append(" ");
			}
			return builder.toString();
		}
	}
	
	private static void allAppend(StringBuilder[] builders, Object str) {
		for(StringBuilder builder : builders) {
			builder.append(str);
		}
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
