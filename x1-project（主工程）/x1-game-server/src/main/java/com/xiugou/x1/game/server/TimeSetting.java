/**
 *
 */
package com.xiugou.x1.game.server;

import java.time.LocalDateTime;

import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class TimeSetting {
    //TODO 几点重置应该读表
    private int oclock = 5;

    public LocalDateTime tomorrowOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.tomorrowZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    public LocalDateTime todayOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.todayZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    public LocalDateTime weekMondayOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.weekMondayZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    public LocalDateTime nextWeekMondayOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.nextWeekMondayZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    public LocalDateTime monthOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.monthZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    public LocalDateTime nextMonthOTime() {
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.nextMonthZeroMillis() + DateTimeUtil.ONE_HOUR_MILLIS * oclock);
    }

    /**
     * 判断是否需要进行数据重置
     * @param resetTime
     * @return
     */
    public boolean needReset(LocalDateTime resetTime) {
        LocalDateTime nowTime = LocalDateTimeUtil.now();
        return nowTime.isAfter(resetTime);
    }

    public int getOclock() {
        return oclock;
    }

    /**
     * 获取某一天的5点（零点）时间
     * @param theTime
     * @return
     */
    public LocalDateTime theDayOTime(LocalDateTime theTime) {
        long theTimeMilli = LocalDateTimeUtil.toEpochMilli(theTime);
        long theTimeZero = DateTimeUtil.somedayZeroMillis(theTimeMilli);
        long theTimeFive = theTimeZero + DateTimeUtil.ONE_HOUR_MILLIS * getOclock();
        if (theTimeMilli < theTimeFive) {
            //5点前创号的角色，到5点之后就算创号第二天
            theTimeFive = theTimeFive - DateTimeUtil.ONE_DAY_MILLIS;
        }
        return LocalDateTimeUtil.ofEpochMilli(theTimeFive);
    }

    /**
     * 某一时刻距离当前时间的天数
     * 游戏里面5点才是“零”点
     * @param theTime
     * @return
     */
    public int daysFromTimeToNow(LocalDateTime theTime) {
        long theTimeMilli = LocalDateTimeUtil.toEpochMilli(theTime);
        long theTimeZero = DateTimeUtil.somedayZeroMillis(theTimeMilli);
        long theTimeFive = theTimeZero + DateTimeUtil.ONE_HOUR_MILLIS * getOclock();
        if (theTimeMilli < theTimeFive) {
            //5点前创号的角色，到5点之后就算创号第二天
            theTimeFive = theTimeFive - DateTimeUtil.ONE_DAY_MILLIS;
        }
        long nowTime = DateTimeUtil.currMillis();
        if(nowTime > theTimeFive) {
        	long day = (nowTime - theTimeFive) / DateTimeUtil.ONE_DAY_MILLIS;
            return (int) (day + 1);
        } else {
        	long day = (nowTime - theTimeFive) / DateTimeUtil.ONE_DAY_MILLIS;
        	return (int)day;
        }
    }

    /**
     * 获取当前时间的下一个5点
     * @return
     */
    public LocalDateTime nextDayOTime() {
        LocalDateTime day5Time = theDayOTime(LocalDateTimeUtil.now());
        return day5Time.plusSeconds(DateTimeUtil.ONE_DAY_SECOND);
    }

    /**
     * 获取某个时间的上一个5点
     * @return
     */
    public LocalDateTime lastDayOTime(LocalDateTime time) {
        LocalDateTime theDay5Time = theDayOTime(time);
        return theDay5Time.minusDays(1);
    }

    /**
     * 获取以当天5点为跨天时间点当前星期几，
     * @return
     */
    public int getWeekDay() {
        LocalDateTime day5Time = theDayOTime(LocalDateTimeUtil.now());
        return day5Time.getDayOfWeek().getValue();
    }

    /**
     * 是否同一天
     * @param time1
     * @param time2
     * @return
     */
    public boolean isSomeDay(LocalDateTime time1, LocalDateTime time2) {
        return LocalDateTimeUtil.isSameDay(theDayOTime(time1), theDayOTime(time2));
    }

    /**
     * 相隔天数
     * @param start
     * @param end
     * @return
     */
    public long daysUntil(LocalDateTime start, LocalDateTime end) {
        return LocalDateTimeUtil.daysUntil(theDayOTime(start), theDayOTime(end));
    }

}
