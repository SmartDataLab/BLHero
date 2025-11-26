package com.xiugou.x1.game.server.module.evil.struct;

import org.gaming.tool.DateTimeUtil;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
public class RefineEvilData {
    private int identity; //id
    private long startTime;//开始时间
    private long endTime; //结束时间

    /**
     * 计算炼妖剩余的分钟数
     * @return
     */
    public long calculateLeftMinute() {
    	long nowTime = DateTimeUtil.currMillis();
    	if(endTime <= nowTime) {
			return 0;
		}
    	long leftMinute = 0;
    	long startTime0 = startTime;
        if (startTime0 <= nowTime) {
        	startTime0 = nowTime;
        }
        leftMinute += (endTime - startTime0) / DateTimeUtil.ONE_MINUTE_MILLIS;
    	if((endTime - startTime0) % DateTimeUtil.ONE_MINUTE_MILLIS != 0) {
    		leftMinute ++;
    	}
        return leftMinute;
    }
    
    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
