/**
 * 
 */
package org.gaming.tool;

import java.text.DecimalFormat;

/**
 * @author YY
 *
 */
public class JvmUtil {

	public static MemoryInfo scanMemory() {
		// 最大可用内存，对应-Xmx
		long maxMemory = Runtime.getRuntime().maxMemory();
		// 当前JVM空闲内存
		long freeMemory = Runtime.getRuntime().freeMemory();
		// 当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		long totalMemory = Runtime.getRuntime().totalMemory();
		long usedMemory = totalMemory - freeMemory;
		long leftMemory = maxMemory - usedMemory;
		
		MemoryInfo memoryInfo = new MemoryInfo();
		memoryInfo.freeMemory = formetSize(freeMemory);
		memoryInfo.maxMemory = formetSize(maxMemory);
		memoryInfo.totalMemory = formetSize(totalMemory);
		memoryInfo.usedMemory = formetSize(usedMemory);
		memoryInfo.leftMemory = formetSize(leftMemory);
		return memoryInfo;
	}

	public static class MemoryInfo {
		private String maxMemory;
		private String freeMemory;
		private String totalMemory;
		private String leftMemory;
		private String usedMemory;
		public String getFreeMemory() {
			return freeMemory;
		}
		public String getMaxMemory() {
			return maxMemory;
		}
		public String getTotalMemory() {
			return totalMemory;
		}
		public String getLeftMemory() {
			return leftMemory;
		}
		public String getUsedMemory() {
			return usedMemory;
		}
	}

	private static String formetSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String sizeString = "";
		if (size < 1024) {
			sizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			sizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			sizeString = df.format((double) size / 1048576) + "M";
		} else {
			sizeString = df.format((double) size / 1073741824) + "G";
		}
		return sizeString;
	}
}
