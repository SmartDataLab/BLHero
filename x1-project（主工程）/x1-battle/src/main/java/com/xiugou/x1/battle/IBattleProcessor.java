/**
 * 
 */
package com.xiugou.x1.battle;

import java.util.List;

import com.google.protobuf.ByteString;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;

import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;
import pb.xiugou.x1.protobuf.battle.Battle.PbSprite;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormationPos;

/**
 * @author YY
 * 需要注意的时间节点
 * 2023-7-5 战斗改成前端做，删除后端部分代码
 * 
 */
public interface IBattleProcessor {
	IBattleType battleType();
	
	BattleContext enter(long playerId, int sceneId, int mainHero, List<PbFormationPos> heroPosList, ByteString params);
	//是否有这个区域
	void hasThisZone(BattleContext context, int zoneId);
	//获取区域数据
	Zone getZoneInfo(BattleContext context, int zoneId);
	//进入区域
	boolean doEnterZone(BattleContext context, int zoneId);
	/**
	 * 
	 * @param context
	 * @param mainHero
	 * @param heroes
	 * @return 返回对长标识
	 */
	int switchHeroes(BattleContext context, int mainHero, List<PbFormationPos> heroPosList);
	//放弃当前战斗
	void onGiveUp(BattleContext context);
	
	//战斗中的精灵是否可以随着时间流逝而复活
	boolean canRebornWithTime();
	
	//处理团队复活
	void doTeamRevive(BattleContext context);
	
	//杀死精灵时
	List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds);
	
	//复活精灵时
	List<PbSprite> onRebornSprite(BattleContext context, List<Integer> spriteIds);
	//检查战斗是否已经结束
	PbBattleEndData checkBattleEnd(BattleContext context);
	
	List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint);
	
	List<Sprite> specialRefreshMonster(BattleContext context, int zoneId);
}
