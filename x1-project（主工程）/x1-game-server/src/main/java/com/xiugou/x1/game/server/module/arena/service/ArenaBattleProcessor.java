/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.battle.constant.SpriteType;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MapMonsterPoint;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.battle.template.TemplateFighter;
import com.xiugou.x1.battle.template.TemplateSprite;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;
import com.xiugou.x1.game.server.foundation.cross.CrossContextManager;
import com.xiugou.x1.game.server.foundation.player.PlayerContextPhantom;
import com.xiugou.x1.game.server.module.arena.model.ArenaPlayer;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.mainline.service.MainlineBattleProcessor;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.arena.Arena.PbArenaBattleParam;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaBattleResult;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaBattleEndRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaBattleEndRpcResponse;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndData;
import pb.xiugou.x1.protobuf.battle.Battle.PbBattleEndStatus;
import pb.xiugou.x1.protobuf.battle.Battle.PbKillSprite;

/**
 * @author hyy
 *
 */
@Component
public class ArenaBattleProcessor extends BaseBattleProcessor<PbArenaBattleParam> {

	@Autowired
	private ArenaPlayerService arenaPlayerService;
	@Autowired
	private MainlineBattleProcessor mainlineBattleProcessor;
	@Autowired
	private PlayerService playerService;
	
	@Override
	public IBattleType battleType() {
		return BattleType.ARENA;
	}

	@Override
	public void hasThisZone(BattleContext context, int zoneId) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(context.getPlayerId());
		Asserts.isTrue(arenaPlayer.getTemplateFighter() != null, TipsCode.ARENA_FIGHTER_NULL);
	}

	@Override
	public Zone getZoneInfo(BattleContext context, int zoneId) {
		//TODO 1001的场景要换一个
		SceneZoneCfg sceneZoneCfg = sceneZoneCache.getInSceneIdZoneIdIndex(1001, zoneId);
        MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());
        Zone zone = context.buildZone(zoneId, mapData);
		return zone;
	}

	@Override
	public void onGiveUp(BattleContext context) {
		//战斗结束的时候移除上下文信息
        playerToContextMap.remove(context.getPlayerId());
        mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
	}

	@Override
	public List<PbKillSprite> onKillSprite(BattleContext context, List<Integer> spriteIds) {
		List<PbKillSprite> result = new ArrayList<>();
        for (int spriteId : spriteIds) {
            Sprite sprite = context.getAllSprites().get(spriteId);
            if (sprite == null) {
                continue;
            }
            sprite.setAlive(false);

            PbKillSprite.Builder builder = PbKillSprite.newBuilder();
            builder.setId(sprite.getId());
            builder.setRebornTime(-1);
            result.add(builder.build());
        }
        return result;
	}

	@Override
	public PbBattleEndData checkBattleEnd(BattleContext context) {
		BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(this.battleType().getValue());
        int battleTime = battleTypeCfg.getBattleTime();  //配置能用时间
        //TODO 时间判断的方案？
        long endTime = DateTimeUtil.currMillis() - context.getStartTime(); //实际使用时间
        if (context.isAtkTeamAllDead() || endTime > battleTime) {
            //战斗结束的时候移除上下文信息
            playerToContextMap.remove(context.getPlayerId());
            mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
            
            ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(context.getPlayerId());
            arenaPlayer.setChallengeNum(arenaPlayer.getChallengeNum() + 1);
            arenaPlayer.setTemplateFighter(null);
            arenaPlayerService.update(arenaPlayer);
            
            Player player = playerService.getEntity(context.getPlayerId());
            PlayerContextPhantom playerContextPhantom = new PlayerContextPhantom(player.getId());
            //发送失败消息至跨服更新
            ArenaBattleEndRpcRequest.Builder rpcRequest = ArenaBattleEndRpcRequest.newBuilder();
            rpcRequest.setPlayerId(player.getId());
            rpcRequest.setServerId(player.getServerId());
            rpcRequest.setResult(2);
            ArenaBattleEndRpcResponse rpcResponse = CrossContextManager.writeSync(playerContextPhantom, ArenaBattleEndRpcRequest.Proto.ID, rpcRequest.build(), ArenaBattleEndRpcResponse.class);
            
            PbArenaBattleResult.Builder pbResult = PbArenaBattleResult.newBuilder();
            pbResult.setBattleTime(endTime);
            pbResult.setScore(rpcResponse.getScore());
//TODO 失败奖励            pbResult.addAllRewards(values);
            
            PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.FAIL);
            builder.setData(pbResult.build().toByteString());
            return builder.build();
        }
        
        if(!context.isDefTeamAllDead()) {
        	PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
            builder.setStatus(PbBattleEndStatus.BATTLING);
            return builder.build();
        }
        
        ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(context.getPlayerId());
        arenaPlayer.getCompetitors().clear();
        arenaPlayer.setChallengeNum(arenaPlayer.getChallengeNum() + 1);
        arenaPlayer.setTemplateFighter(null);
        arenaPlayerService.update(arenaPlayer);
        
        Player player = playerService.getEntity(context.getPlayerId());
        PlayerContextPhantom playerContextPhantom = new PlayerContextPhantom(player.getId());
        //发送胜利消息至跨服更新
        ArenaBattleEndRpcRequest.Builder rpcRequest = ArenaBattleEndRpcRequest.newBuilder();
        rpcRequest.setPlayerId(player.getId());
        rpcRequest.setServerId(player.getServerId());
        rpcRequest.setResult(2);
        ArenaBattleEndRpcResponse rpcResponse = CrossContextManager.writeSync(playerContextPhantom, ArenaBattleEndRpcRequest.Proto.ID, rpcRequest.build(), ArenaBattleEndRpcResponse.class);
	
		// 战斗结束的时候移除上下文信息
		playerToContextMap.remove(context.getPlayerId());
		mainlineBattleProcessor.returnToMainlineScene(context.getPlayerId());
		
		PbArenaBattleResult.Builder pbResult = PbArenaBattleResult.newBuilder();
        pbResult.setBattleTime(endTime);
        pbResult.setScore(rpcResponse.getScore());
//TODO 胜利奖励            pbResult.addAllRewards(values);
        
		PbBattleEndData.Builder builder = PbBattleEndData.newBuilder();
		builder.setStatus(PbBattleEndStatus.WIN);
		builder.setData(pbResult.build().toByteString());
        return builder.build();
	}

	@Override
	public List<Sprite> refreshMonster(BattleContext context, int zoneId, MapMonsterPoint monsterPoint) {
		return Collections.emptyList();
	}
	
	@Override
	public List<Sprite> specialRefreshMonster(BattleContext context, int zoneId) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(context.getPlayerId());
		TemplateFighter templateFighter = arenaPlayer.getTemplateFighter();
		SpriteType spriteType = SpriteType.valueOf(templateFighter.getType());
		List<Sprite> sprites = new ArrayList<>();
		for(TemplateSprite templateSprite : templateFighter.getSprites()) {
			Sprite sprite = context.spawnFromTemplate(templateSprite, TeamSide.DEF, spriteType);
			sprites.add(sprite);
		}
        return sprites;
	}
	

	@Override
	protected void canEnter(long playerId, int sceneId, PbArenaBattleParam params) {
		BattleContext context = getContext(playerId);
        Asserts.isTrue(context == null, TipsCode.TOWER_CHALLENGING);
	}

	@Override
	protected void doEnterSetParam(BattleContext context, PbArenaBattleParam params) {
		context.setStartTime(DateTimeUtil.currMillis());
	}

	@Override
	protected PbArenaBattleParam parseParams(ByteString params) {
		try {
			return PbArenaBattleParam.parseFrom(params);
		} catch (InvalidProtocolBufferException e) {
			return PbArenaBattleParam.getDefaultInstance();
		}
	}
}
