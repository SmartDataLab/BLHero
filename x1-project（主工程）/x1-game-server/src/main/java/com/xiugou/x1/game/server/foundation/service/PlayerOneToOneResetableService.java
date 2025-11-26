/**
 *
 */
package com.xiugou.x1.game.server.foundation.service;

import java.time.LocalDateTime;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.game.server.TimeSetting;

/**
 * @author YY
 *
 */
public abstract class PlayerOneToOneResetableService<T extends AbstractEntity> extends PlayerOneToOneService<T> {

    @Autowired
    protected TimeSetting timeSetting;

    @Override
    public T getEntity(long entityId) {
        T entity = super.getEntity(entityId);

        boolean needUpdate = false;
        LocalDateTime now = LocalDateTimeUtil.now();
        // 每月重置
        if (entity instanceof MonthlyResetEntity) {
            MonthlyResetEntity monthlyEntity = (MonthlyResetEntity) entity;
            if (now.isAfter(monthlyEntity.getMonthlyTime())) {
                monthlyEntity.setMonthlyTime(timeSetting.nextMonthOTime());

                doMonthlyReset(entity);
                needUpdate = true;
            }
        }
        // 每周重置
        if (entity instanceof WeeklyResetEntity) {
            WeeklyResetEntity weeklyEntity = (WeeklyResetEntity) entity;
            if (now.isAfter(weeklyEntity.getWeeklyTime())) {
                weeklyEntity.setWeeklyTime(timeSetting.nextWeekMondayOTime());

                doWeeklyReset(entity);
                needUpdate = true;
            }
        }
        // 每日数据重置
        if (entity instanceof DailyResetEntity) {
            DailyResetEntity dailyEntity = (DailyResetEntity) entity;
            if (now.isAfter(dailyEntity.getDailyTime())) {
                dailyEntity.setDailyTime(timeSetting.nextDayOTime());

                doDailyReset(entity);
                needUpdate = true;
            }
        }
        // 个性化重置
        if(doSpecialReset(entity)) {
        	needUpdate = true;
        }
        if (needUpdate) {
            this.update(entity);
        }
        return entity;
    }

    protected void doDailyReset(T entity) {
        //to be override
    }

    protected void doWeeklyReset(T entity) {
        //to be override
    }

    protected void doMonthlyReset(T entity) {
        //to be override
    }
    
    protected boolean doSpecialReset(T entity) {
    	//to be override
    	return false;
    }

    public static interface DailyResetEntity {
        /**
         * 每日重置时间
         * @return
         */
        LocalDateTime getDailyTime();

        void setDailyTime(LocalDateTime dailyTime);
    }

    public static interface WeeklyResetEntity {
        /**
         * 每周重置时间
         * @return
         */
        LocalDateTime getWeeklyTime();

        void setWeeklyTime(LocalDateTime weeklyTime);
    }

    public static interface MonthlyResetEntity {
        /**
         * 每月重置时间
         * @return
         */
        LocalDateTime getMonthlyTime();

        void setMonthlyTime(LocalDateTime monthlyTime);
    }
}
