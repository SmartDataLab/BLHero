/**
 * 
 */
package org.gaming.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YY
 *
 */
public class DateTimeUtil {

	
	/**
	 * 一秒
	 */
	public static final int ONE_SECOND_SECOND = 1;
	public static final long ONE_SECOND_MILLIS = 1000L;
	/**
	 * 一分钟
	 */
	public static final int ONE_MINUTE_SECOND = 60;
	public static final long ONE_MINUTE_MILLIS = 60 * ONE_SECOND_MILLIS;
	/**
	 * 一小时
	 */
	public static final int ONE_HOUR_SECOND = 60 * ONE_MINUTE_SECOND;
	public static final long ONE_HOUR_MILLIS = 60 * ONE_MINUTE_MILLIS;
	/**
	 * 一天
	 */
	public static final int ONE_DAY_SECOND = 24 * ONE_HOUR_SECOND;
	public static final long ONE_DAY_MILLIS = 24 * ONE_HOUR_MILLIS;
	/**
	 * 一周
	 */
	public static final int ONE_WEEK_SECOND = 7 * ONE_DAY_SECOND;
	public static final long ONE_WEEK_MILLIS = 7 * ONE_DAY_MILLIS;
	
	/**
     * yyyy-MM-dd HH:mm:ss 格式
     */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String HH_MM = "HH:mm";
	public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	
	/**
	 * 服务器时间的毫秒偏移量，用来做服务器时间调整
	 */
	private static long offset = 0;
	
	// 当天零点时间戳，单位秒
    private static volatile int todayZeroSecond = 0;
    // 明天零点时间戳，单位秒
    private static volatile int tomorrowZeroSecond = 0;
    // 昨天零点时间戳，单位秒
    private static volatile int yesterdayZeroSecond = 0;

    // 当天零点时间戳，单位毫秒
    private static volatile long todayZeroMillis = 0;
    // 明天零点时间戳，单位毫秒
    private static volatile long tomorrowZeroMillis = 0;
    // 昨天零点时间戳，单位毫秒
    private static volatile long yesterdayZeroMillis = 0;
	
	/**
	 * 当前时间的毫秒数
	 * @return
	 */
	public static long currMillis() {
		return offset + System.currentTimeMillis();
	}
	/**
	 * 当前时间的秒数
	 * @return
	 */
	public static int currSecond() {
		return (int)(currMillis() / 1000);
	}
	
	/**
	 * 重置零点
	 */
	private static synchronized void resetZero() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long tempDayZeroMillis = cal.getTimeInMillis();
        int tempDayZeroSecond = (int) (tempDayZeroMillis / ONE_SECOND_MILLIS);

        todayZeroSecond = tempDayZeroSecond;
        tomorrowZeroSecond = tempDayZeroSecond + ONE_DAY_SECOND;
        yesterdayZeroSecond = tempDayZeroSecond - ONE_DAY_SECOND;

        todayZeroMillis = tempDayZeroMillis;
        tomorrowZeroMillis = tempDayZeroMillis + ONE_DAY_MILLIS;
        yesterdayZeroMillis = tempDayZeroMillis - ONE_DAY_MILLIS;
    }
	
	private static void checkResetZero() {
		// 当前时间戳大于记录的明天零点时间戳时重置零点时间戳
		if (currMillis() >= tomorrowZeroMillis || todayZeroSecond == 0 || tomorrowZeroSecond == 0
				|| yesterdayZeroSecond == 0 || todayZeroMillis == 0 || tomorrowZeroMillis == 0
				|| yesterdayZeroMillis == 0) {
			resetZero();
		}
	}
	
	/**
     * 获得今天0点的时间戳，秒数
     * @return
     */
    public static int todayZeroSecond() {
    	checkResetZero();
        return todayZeroSecond;
    }
    /**
     * 获得今天0点的时间戳，毫秒数
     * @return
     */
    public static long todayZeroMillis() {
    	checkResetZero();
        return todayZeroMillis;
    }
    
    /**
     * 获得明天0点的时间戳，秒数
     * @return
     */
    public static int tomorrowZeroSecond() {
    	checkResetZero();
        return tomorrowZeroSecond;
    }
    /**
     * 获得明天0点的时间戳，毫秒数
     * @return
     */
    public static long tomorrowZeroMillis() {
    	checkResetZero();
        return tomorrowZeroMillis;
    }
    
    /**
     * 获得昨天0点的时间戳，秒数
     * @return
     */
    public static int yesterdayZeroSecond() {
    	checkResetZero();
        return yesterdayZeroSecond;
    }
    /**
     * 获得昨天0点的时间戳，毫秒数
     * @return
     */
    public static long yesterdayZeroMillis() {
    	checkResetZero();
        return yesterdayZeroMillis;
    }
    
    /**
     * 获取某个时间所在的月份
     * @param timeMillis
     * @return
     */
    public static int monthOfMillis(long timeMillis) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(timeMillis);
    	return cal.get(Calendar.MONTH);
    }
    
    /**
     * 月初的毫秒时间
     * @return
     */
    public static long monthZeroMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	int thisMonth = cal.get(Calendar.MONTH);
    	cal.set(Calendar.MONTH, thisMonth);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    /**
     * 下一个月月初的毫秒时间
     * @return
     */
    public static long nextMonthZeroMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	int nextMonth = cal.get(Calendar.MONTH) + 1;
    	cal.set(Calendar.MONTH, nextMonth);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    /**
     * 获取某个月的零点毫秒时间
     * @param month
     * @return
     */
    public static long monthZeroMillis(int month) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	cal.set(Calendar.MONTH, month);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    /**
     * 周日零点
     * @return
     */
    public static long weekSundayZeroMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	int thisWeek = cal.get(Calendar.WEEK_OF_YEAR);
    	cal.set(Calendar.WEEK_OF_YEAR, thisWeek);
    	cal.set(Calendar.DAY_OF_WEEK, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    /**
     * 周一零点
     * @return
     */
    public static long weekMondayZeroMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	int thisWeek = cal.get(Calendar.WEEK_OF_YEAR);
    	cal.set(Calendar.WEEK_OF_YEAR, thisWeek);
    	cal.set(Calendar.DAY_OF_WEEK, 2);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    public static long nextWeekMondayZeroMillis() {
    	long thisWeekMonday = weekMondayZeroMillis();
    	return thisWeekMonday + ONE_WEEK_MILLIS;
    }
	
    /**
     * 获取任意一个时间点当天的零时时间
     *
     * @param theDay
     * @return
     */
    public static long somedayZeroMillis(long someTime) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(someTime);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
	
    private static final ThreadLocal<ConcurrentHashMap<String, SimpleDateFormat>> LOCAL_FORMATER = ThreadLocal.withInitial(ConcurrentHashMap::new);

    private static SimpleDateFormat getFormat(String format) {
        return LOCAL_FORMATER.get().computeIfAbsent(format, DateTimeUtil::makeSimpleDateFormat);
    }
	
    private static SimpleDateFormat makeSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }
    
    /**
     * 将毫秒时间转化为指定格式的日期字符串
     * @param format
     * @param timeMillis
     * @return
     */
    public static String formatMillis(String format, long millisTime) {
        return getFormat(format).format(millisTime);
    }
    
    /**
     * 下一个小时
     * @return
     */
    public static long nextHourMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
    	cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    /**
     * 当前小时
     * @return
     */
    public static long currHourMillis() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(currMillis());
    	cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
    	cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTimeInMillis();
    }
    
    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     *
     * @param str    字符串
     * @param format 日期格式
     * @return 日期
     */
    public static Date stringToDate(String format, String timeString) {
        if (null == timeString || "".equals(timeString)) {
            return new Date();
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format)) {
            format = YYYY_MM_DD_HH_MM_SS;
        }
        try {
        	SimpleDateFormat sdf = getFormat(format);
            return sdf.parse(timeString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }
    
    public static long stringToMillis(String format, String timeString) {
    	return stringToDate(format, timeString).getTime();
    }
    
    public static void setOffset(long offset) {
    	DateTimeUtil.offset = offset;
    }
    
	public static void main(String[] args) {
		System.out.println(nextHourMillis());
		System.out.println(formatMillis(HH_MM, 1800000));
		
		offset = ONE_DAY_MILLIS * 2;
		
//		resetZero();
//		System.out.println(todayZeroSecond + " " + new Date(todayZeroMillis));
//		System.out.println(tomorrowZeroSecond + " " + new Date(tomorrowZeroMillis));
//		System.out.println(yesterdayZeroSecond + " " + new Date(yesterdayZeroMillis));
//		
//		System.out.println(todayZeroMillis + " " + new Date(todayZeroMillis));
//		System.out.println(tomorrowZeroMillis + " " + new Date(tomorrowZeroMillis));
//		System.out.println(yesterdayZeroMillis + " " + new Date(yesterdayZeroMillis));
	}
}
