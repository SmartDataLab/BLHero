/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;

import pb.xiugou.x1.protobuf.arena.Arena.PbArenaBattleParam;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;

/**
 * @author hyy
 *
 */
@Component
public class ArenaDefBattleProcessor extends BaseBattleProcessor<PbArenaBattleParam> {

	@Override
	public IBattleType battleType() {
		return BattleType.ARENA_DEF;
	}

	@Override
	public void hasThisZone(BattleContext context, int zoneId) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public Zone getZoneInfo(BattleContext context, int zoneId) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public void onGiveUp(BattleContext context) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public PbBattleEndData checkBattleEnd(BattleContext context) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}
	
	@Override
	public List<Sprite> specialRefreshMonster(BattleContext context, int zoneId) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}
	
	@Override
	protected void canEnter(long playerId, int sceneId, PbArenaBattleParam params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	protected void doEnterSetParam(BattleContext context, PbArenaBattleParam params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	protected PbArenaBattleParam parseParams(ByteString params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}
}
