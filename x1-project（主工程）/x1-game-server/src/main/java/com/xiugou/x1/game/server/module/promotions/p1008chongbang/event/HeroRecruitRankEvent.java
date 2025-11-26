/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang.event;

/**
 * @author YY
 *
 */
public class HeroRecruitRankEvent {
	private long playerId;
	private int typeId;
	
	public static HeroRecruitRankEvent of(long playerId, int typeId) {
		HeroRecruitRankEvent event = new HeroRecruitRankEvent();
		event.playerId = playerId;
		event.typeId = typeId;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	public int getTypeId() {
		return typeId;
	}
}
