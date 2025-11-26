/**
 *
 */
package com.xiugou.x1.game.server.module.battle;

import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleProcessor;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.battle.translate.BattleTranslate;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.battle.processor.BattleManager;

import pb.xiugou.x1.protobuf.battle.Battle.BattleGiveUpRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleGiveUpResponse;
import pb.xiugou.x1.protobuf.battle.Battle.BattleKillSpriteRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleKillSpriteResponse;
import pb.xiugou.x1.protobuf.battle.Battle.BattleRebornSpriteRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleRebornSpriteResponse;
import pb.xiugou.x1.protobuf.battle.Battle.BattleReviveRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleReviveResponse;
import pb.xiugou.x1.protobuf.battle.Battle.BattleSettleRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleSettleResponse;
import pb.xiugou.x1.protobuf.battle.Battle.BattleSyncKillMonsterRequest;
import pb.xiugou.x1.protobuf.battle.Battle.BattleSyncKillMonsterResponse;
import pb.xiugou.x1.protobuf.battle.Battle.EnterBattleRequest;
import pb.xiugou.x1.protobuf.battle.Battle.EnterBattleResponse;
import pb.xiugou.x1.protobuf.battle.Battle.EnterZoneRequest;
import pb.xiugou.x1.protobuf.battle.Battle.EnterZoneResponse;
import pb.xiugou.x1.protobuf.battle.Battle.SwitchBattleHeroRequest;
import pb.xiugou.x1.protobuf.battle.Battle.SwitchBattleHeroResponse;
import pb.xiugou.x1.protobuf.battle.Battle.ZoneInfoRequest;
import pb.xiugou.x1.protobuf.battle.Battle.ZoneInfoResponse;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormationPos;

/**
 * @author YY
 *
 */
@Controller
public class BattleHandler {
	
	static Logger logger = LoggerFactory.getLogger(BattleHandler.class);

	@PlayerCmd
    public EnterBattleResponse enterBattle(PlayerContext playerContext, EnterBattleRequest request) {
        BattleType battleType = BattleType.valueOf(request.getBattleType());
        Asserts.isTrue(battleType != null, TipsCode.BATTLE_TYPE_WRONG);
        BaseBattleProcessor<?> processor = BattleManager.getProcessor(battleType);
        
        System.out.println("EnterBattleRequest1 " + request.getBattleType() + " " + request.getMapId());
        
        List<PbFormationPos> posList = request.getPosListList();
        BattleContext context = processor.enter(playerContext.getId(), request.getMapId(), request.getMainHero(), posList, request.getBattleParams());

        System.out.println("EnterBattleRequest2 " + request.getBattleType() + " " + context.getSceneId());
        
        EnterBattleResponse.Builder response = EnterBattleResponse.newBuilder();
        response.setBattleType(request.getBattleType());
        response.setMapId(request.getMapId());
        for (Sprite hero : context.getAtkTeam().getAllSpriteMap().values()) {
            response.addHeroes(hero.build());
        }
        response.setMainHero(context.getMainHeroIdentity());
        response.setBattleParams(context.getClientParams());
        response.setType(request.getType());
        return response.build();
    }

	@PlayerCmd
    public EnterZoneResponse enterZone(PlayerContext playerContext, EnterZoneRequest request) {
//    	System.out.println("请求进入区域 " + playerContext.getId() + " zone_id参数" + request.getZoneId());
    	
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        processor.hasThisZone(context, request.getZoneId());

//        System.out.println("进入区域成功 " + playerContext.getId() + " zone_id参数" + request.getZoneId());
        
        boolean enter = processor.doEnterZone(context, request.getZoneId());
        Asserts.isTrue(enter, TipsCode.BATTLE_ZONE_NOT_INIT, request.getZoneId());
        
        EnterZoneResponse.Builder response = EnterZoneResponse.newBuilder();
        response.setZoneId(request.getZoneId());
        return response.build();
    }
    
	@PlayerCmd
    public ZoneInfoResponse zoneInfo(PlayerContext playerContext, ZoneInfoRequest request) {
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        
//        System.out.println("请求区域信息 " + playerContext.getId() + " zone_id参数" + request.getZoneId() + " " + context.getBattleType().getDesc());
        
        IBattleProcessor processor = context.getBattleProcessor();
        processor.hasThisZone(context, request.getZoneId());
        
        Zone zone = processor.getZoneInfo(context, request.getZoneId());
        System.out.println("场景" + context.getSceneId() + "区域" + zone.getId() + "怪物数量" + zone.getSprites().size());
        
        ZoneInfoResponse.Builder response = ZoneInfoResponse.newBuilder();
        response.setZone(BattleTranslate.build(zone));
        return response.build();
    }

	@PlayerCmd
    public SwitchBattleHeroResponse switchHero(PlayerContext playerContext, SwitchBattleHeroRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        
        List<PbFormationPos> posList = request.getPosListList();
        int mainHero = processor.switchHeroes(context, request.getMainHero(), posList);

        SwitchBattleHeroResponse.Builder response = SwitchBattleHeroResponse.newBuilder();
        for (Sprite hero : context.getAtkTeam().getAllSpriteMap().values()) {
            response.addHeroes(hero.build());
        }
        response.setMainHero(mainHero);
        return response.build();
    }

    /**
     * 复活流程->所有攻方单位死亡->主线玩法->判断是否在露营中->是则等待自然复活
     * 											 ->否则看有没有复活次数->有则暂停复活确认
     * 															  ->无则返回村庄安全区
     *                     ->非主线玩法->退出副本并返回主线场景
     * @param playerContext
     * @param request
     * @return
     */
	@PlayerCmd
    public BattleReviveResponse revive(PlayerContext playerContext, BattleReviveRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        processor.doTeamRevive(context);
        //进行复活
        context.reviveAllHero();
        BattleReviveResponse.Builder response = BattleReviveResponse.newBuilder();
        for (Sprite sprite : context.getAtkTeam().getAllSpriteMap().values()) {
            response.addHeroes(sprite.build());
        }
        return response.build();
    }

	@PlayerCmd
    public BattleGiveUpResponse giveUp(PlayerContext playerContext, BattleGiveUpRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        processor.onGiveUp(context);
        return BattleGiveUpResponse.getDefaultInstance();
    }
    
	@PlayerCmd
    public BattleKillSpriteResponse killSprite(PlayerContext playerContext, BattleKillSpriteRequest request) {
    	//System.out.println("killSprite");
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
    	Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
    	IBattleProcessor processor = context.getBattleProcessor();
    	
    	BattleKillSpriteResponse.Builder response = BattleKillSpriteResponse.newBuilder();
    	response.setSerialId(request.getSerialId());
    	response.addAllSprites(processor.onKillSprite(context, request.getSpriteIdsList()));
    	response.setEndData(processor.checkBattleEnd(context));
    	return response.build();
    }

	@PlayerCmd
    public BattleSettleResponse settle(PlayerContext playerContext, BattleSettleRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        BattleSettleResponse.Builder response = BattleSettleResponse.newBuilder();
        response.setEndData(processor.checkBattleEnd(context));
        //System.out.println("settle");
        return response.build();
    }
    
    /**
     * 复活英雄
     * @param playerContext
     * @param request
     * @return
     */
	@PlayerCmd
    public BattleRebornSpriteResponse rebornSprite(PlayerContext playerContext, BattleRebornSpriteRequest request) {
    	BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
    	Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
    	IBattleProcessor processor = context.getBattleProcessor();
    	
    	BattleRebornSpriteResponse.Builder response = BattleRebornSpriteResponse.newBuilder();
    	response.addAllSprites(processor.onRebornSprite(context, request.getSpriteIdsList()));
    	return response.build();
    }
    
	@PlayerCmd
    public BattleSyncKillMonsterResponse syncKillMonster(PlayerContext playerContext, BattleSyncKillMonsterRequest request) {
    	
    	
    	
    	return BattleSyncKillMonsterResponse.getDefaultInstance();
    }
}
