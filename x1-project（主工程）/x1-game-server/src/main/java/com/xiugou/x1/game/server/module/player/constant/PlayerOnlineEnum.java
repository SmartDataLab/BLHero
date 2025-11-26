package com.xiugou.x1.game.server.module.player.constant;

import org.gaming.tool.DateTimeUtil;

/**
 * @author yh
 * @date 2023/7/20
 * @apiNote
 */
public enum PlayerOnlineEnum {
	FIVE_MINUTES(5,"0-5分钟"),
	TEN_MINUTES(10,"5-10分钟"),
	FIFTEEN_MINUTES(15,"10-15分钟"),
	TWENTY_MINUTES(20,"15-20分钟"),
	TWENTY_FIVE_MINUTES(25,"20-25分钟"),
	THIRTY_MINUTES(30,"25-30分钟"),
	FORTY_MINUTES(40,"30-40分钟"),
	FIFTY_MINUTES(50,"40-60分钟"),
	SIXTY_MINUTES(60,"50-60分钟"),
	TWO_HOURS(120,"1-2小时"),
	FOUR_HOURS(240,"2-4小时"),
	SIX_HOURS(360,"4-6小时"),
	EIGHT_HOURS(480,"6-8小时"),
	TEN_HOURS(600,"8-10小时"),
	HIGH_TEN_HOURS(14400,"10小时以上"),
	;
	private int time;

	private String desc;

	private PlayerOnlineEnum(int time, String desc) {
		this.time = time;
		this.desc = desc;
	}
	public int getTime() {
		return time * DateTimeUtil.ONE_MINUTE_SECOND;
	}
	public String getDesc(){
		return desc;
	}

}
