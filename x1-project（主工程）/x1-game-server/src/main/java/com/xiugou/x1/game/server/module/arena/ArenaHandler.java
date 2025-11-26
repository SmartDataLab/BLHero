/**
 * 
 */
package com.xiugou.x1.game.server.module.arena;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.fakecmd.annotation.PlayerToOtherRequestCmd;
import org.gaming.fakecmd.annotation.PlayerToOtherResponseCmd;
import org.gaming.fakecmd.side.game.IPlayer;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.template.TemplateFighter;
import com.xiugou.x1.battle.template.TemplateSprite;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ArenaDailyRewardCache;
import com.xiugou.x1.design.module.ArenaSeasonRewardCache;
import com.xiugou.x1.design.module.autogen.ArenaDailyRewardAbstractCache.ArenaDailyRewardCfg;
import com.xiugou.x1.design.module.autogen.ArenaSeasonRewardAbstractCache.ArenaSeasonRewardCfg;
import com.xiugou.x1.game.server.foundation.cross.CrossContext;
import com.xiugou.x1.game.server.foundation.cross.CrossContextManager;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.arena.model.ArenaPlayer;
import com.xiugou.x1.game.server.module.arena.model.ArenaRecord;
import com.xiugou.x1.game.server.module.arena.service.ArenaPlayerService;
import com.xiugou.x1.game.server.module.arena.service.ArenaRecordService;
import com.xiugou.x1.game.server.module.arena.struct.ArenaCompetitor;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.formation.struct.HeroPos;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.arena.Arena.ArenaChallengeRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaChallengeResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaInfoRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaInfoResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaRecordRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaRecordResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaRefreshRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaRefreshResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaResumeInfoRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaResumeInfoResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaTopInfoRequest;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaFighter;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaHero;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaRanker;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaRecord;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeFighterRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeFighterRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeRobotRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeRobotRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaDailySettleRpcMessage;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaInfoRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaInfoRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaRefreshRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaRefreshRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaResumeInfoRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaResumeInfoRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaSeasonSettleRpcMessage;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.PbArenaSettleRanker;

/**
 * @author hyy
 *
 */
@Controller
public class ArenaHandler {

	private static Logger logger = LoggerFactory.getLogger(ArenaHandler.class);
	
	@Autowired
	private ArenaRecordService arenaRecordService;
	@Autowired
	private ArenaSeasonRewardCache arenaSeasonRewardCache;
	@Autowired
	private ArenaDailyRewardCache arenaDailyRewardCache;
	@Autowired
	private MailService mailService;
	@Autowired
	private ArenaPlayerService arenaPlayerService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private FormationService formationService;
	@Autowired
	private HeroService heroService;
	
	@PlayerCmd
	public void topInfo(PlayerContext playerContext, ArenaTopInfoRequest request) {
		CrossContextManager.write(playerContext, ArenaTopInfoRequest.Proto.ID, request);
	}
	
	@PlayerCmd
	public void resumeInfo(PlayerContext playerContext, ArenaResumeInfoRequest request) {
		ArenaResumeInfoRpcRequest.Builder rpcRequest = ArenaResumeInfoRpcRequest.newBuilder();
		rpcRequest.setServerId(playerContext.getServerId());
		CrossContextManager.write(playerContext, ArenaResumeInfoRpcRequest.Proto.ID, rpcRequest.build());
	}
	
	@PlayerCrossCmd
	public ArenaResumeInfoResponse resumeInfo(long playerId, ArenaResumeInfoRpcResponse rpcResponse) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerId);
		if(arenaPlayer.getScore() != rpcResponse.getScore()) {
			arenaPlayer.setScore(rpcResponse.getScore());
			arenaPlayerService.update(arenaPlayer);
		}
		
		ArenaResumeInfoResponse.Builder response = ArenaResumeInfoResponse.newBuilder();
		response.setRank(rpcResponse.getRank());
		response.setScore(rpcResponse.getScore());
		response.setSettleTime(rpcResponse.getSettleTime());
		return response.build();
	}
	
	
	@PlayerCmd
	public void info(PlayerContext playerContext, ArenaInfoRequest request) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerContext.getId());
		
		ArenaInfoRpcRequest.Builder rpcRequest = ArenaInfoRpcRequest.newBuilder();
		rpcRequest.setServerId(playerContext.getServerId());
		if(DateTimeUtil.currMillis() > arenaPlayer.getAutoRefreshTime() || arenaPlayer.getCompetitors().isEmpty()) {
			rpcRequest.setRefresh(true);
		}
		CrossContextManager.write(playerContext, ArenaInfoRpcRequest.Proto.ID, rpcRequest.build());
	}
	
	@PlayerCrossCmd
	public ArenaInfoResponse info(long playerId, ArenaInfoRpcResponse rpcResponse) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerId);
		
		ArenaInfoResponse.Builder builder = ArenaInfoResponse.newBuilder();
		builder.addAllTopPlayers(rpcResponse.getTopPlayersList());
		builder.setMyRank(rpcResponse.getMyRank());
		builder.addAllCompetitors(rpcResponse.getCompetitorsList());
		builder.setBuyNum(arenaPlayer.getBuyNum());
		builder.setChallengeNum(arenaPlayer.getChallengeNum());
		builder.setRefreshTime(arenaPlayer.getHandRefreshTime());
		if(builder.getCompetitorsList().size() > 0) {
			arenaPlayer.getCompetitors().clear();
			for(PbArenaRanker pbArenaRanker : builder.getCompetitorsList()) {
				ArenaCompetitor competitor = new ArenaCompetitor();
				competitor.setRank(pbArenaRanker.getRank());
				competitor.setId(pbArenaRanker.getPlayerId());
				competitor.setServerZone(pbArenaRanker.getServerId());
				competitor.setNick(pbArenaRanker.getNick());
				competitor.setScore(pbArenaRanker.getScore());
				competitor.setFighting(pbArenaRanker.getFighting());
				competitor.setHead(pbArenaRanker.getHead());
				competitor.setImage(pbArenaRanker.getImage());
				arenaPlayer.getCompetitors().add(competitor);
			}
			arenaPlayer.setAutoRefreshTime(DateTimeUtil.currMillis() + DateTimeUtil.ONE_MINUTE_MILLIS * 30);
			arenaPlayerService.update(arenaPlayer);
		} else {
			//取本地数据
			for(ArenaCompetitor competitor : arenaPlayer.getCompetitors()) {
				PbArenaRanker.Builder pbRanker = PbArenaRanker.newBuilder();
				pbRanker.setRank(competitor.getRank());
				pbRanker.setPlayerId(competitor.getId());
				pbRanker.setServerId(competitor.getServerZone());
				pbRanker.setNick(competitor.getNick());
				pbRanker.setScore(competitor.getScore());
				pbRanker.setFighting(competitor.getFighting());
				pbRanker.setHead(competitor.getHead());
				pbRanker.setImage(competitor.getImage());
				builder.addCompetitors(pbRanker.build());
			}
		}
		return builder.build();
	}
	
	@PlayerCmd
	public void challenge(PlayerContext playerContext, ArenaChallengeRequest request) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerContext.getId());
		ArenaCompetitor target = null;
		for(ArenaCompetitor competitor : arenaPlayer.getCompetitors()) {
			if(competitor.getId() == request.getPlayerId() && competitor.getServerZone() == request.getServerId()) {
				target = competitor;
				break;
			}
		}
		Asserts.isTrue(target != null, TipsCode.ARENA_COMPETITOR_MISS);
		
    	FormationResult formationResult = formationService.buildFormationResult(playerContext.getId(), request.getMainHero(), request.getPosListList());
    	formationService.saveFormation(playerContext.getId(), BattleType.ARENA, formationResult.getMainHero(), formationResult.getList());
		
		if(target.getServerZone() <= 0) {
			//机器人
			ArenaChallengeRobotRpcRequest.Builder rpcRequest = ArenaChallengeRobotRpcRequest.newBuilder();
			rpcRequest.setPlayerId(request.getPlayerId());
			rpcRequest.setServerId(0);
			CrossContextManager.write(playerContext, ArenaChallengeRobotRpcRequest.Proto.ID, rpcRequest.build());
		} else {
			//玩家
			ArenaChallengeFighterRpcRequest.Builder rpcRequest = ArenaChallengeFighterRpcRequest.newBuilder();
			rpcRequest.setPlayerId(target.getId());
			rpcRequest.setServerId(target.getServerZone());
			CrossContextManager.jumpRequest(playerContext, target, ArenaChallengeFighterRpcRequest.Proto.ID, rpcRequest.build());
		}
	}
	
	@PlayerCrossCmd
	public ArenaChallengeResponse challenge(long playerId, ArenaChallengeRobotRpcResponse rpcResponse) {
		TemplateFighter defender = GsonUtil.parseJson(rpcResponse.getFighterJson(), TemplateFighter.class);
		
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerId);
		arenaPlayer.setTemplateFighter(defender);
		arenaPlayerService.update(arenaPlayer);
		
		ArenaCompetitor target = null;
		for(ArenaCompetitor competitor : arenaPlayer.getCompetitors()) {
			if(competitor.getId() == defender.getId() && competitor.getServerZone() == defender.getServerId()) {
				target = competitor;
				break;
			}
		}
		
		Formation formation = formationService.getEntity(playerId, BattleType.ARENA);
		if(formation.getPositions().isEmpty()) {
			formation = formationService.getEntity(playerId, BattleType.MAINLINE);
		}
		TemplateFighter attacker = formationService.buildFighter(formation);
		
		ArenaChallengeResponse.Builder response = ArenaChallengeResponse.newBuilder();
		response.setAttacker(build(attacker, arenaPlayer.getScore()));
		response.setDefender(build(defender, target == null ? 0 : target.getScore()));
		return response.build();
	}
	
	public PbArenaFighter build(TemplateFighter fighter, long score) {
		PbArenaFighter.Builder builder = PbArenaFighter.newBuilder();
		builder.setNick(fighter.getNick());
		builder.setFighting(fighter.getFighting());
		builder.setScore(score);
		for(TemplateSprite sprite : fighter.getSprites()) {
			PbArenaHero.Builder data = PbArenaHero.newBuilder();
			data.setIdentity(sprite.getIdentity());
			data.setPos(sprite.getPos());
			builder.addHeroPoss(data.build());
		}
		return builder.build();
	}
	
	@PlayerToOtherRequestCmd
	public ArenaChallengeFighterRpcResponse challenge(IPlayerContext playerContext, IPlayer otherPlayer, ArenaChallengeFighterRpcRequest rpcRequest) {
		Player player = playerService.getEntity(playerContext.getId());
		
		TemplateFighter templateFighter = new TemplateFighter();
		templateFighter.setId(player.getId());
		templateFighter.setNick(player.getNick());
		templateFighter.setLevel(player.getLevel());
		templateFighter.setServerId(player.getServerId());
		templateFighter.setType(1);
		templateFighter.setFighting(player.getFighting());
		Formation formation = formationService.getEntity(playerContext.getId(), BattleType.ARENA_DEF);
		if(formation.getPositions().isEmpty()) {
			formation = formationService.getEntity(playerContext.getId(), BattleType.ARENA);
		}
		for(HeroPos heroPos : formation.getPositions().values()) {
			Hero hero = heroService.getEntity(playerContext.getId(), heroPos.getIdentity());
			TemplateSprite templateSprite = new TemplateSprite();
			templateSprite.setIdentity(hero.getIdentity());
			templateSprite.setLevel(hero.getLevel());
			templateSprite.setPanelAttr(hero.getPanelAttr());
			templateFighter.getSprites().add(templateSprite);
		}
		
		ArenaChallengeFighterRpcResponse.Builder rpcResponse = ArenaChallengeFighterRpcResponse.newBuilder();
		rpcResponse.setPlayerId(player.getId());
		rpcResponse.setServerId(player.getServerId());
		rpcResponse.setFighterJson(GsonUtil.toJson(templateFighter));
		return rpcResponse.build();
	}
	
	@PlayerToOtherResponseCmd
	public ArenaChallengeResponse challenge(IPlayerContext playerContext, IPlayer otherPlayer, ArenaChallengeFighterRpcResponse rpcResponse) {
		TemplateFighter defender = GsonUtil.parseJson(rpcResponse.getFighterJson(), TemplateFighter.class);
		
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerContext.getId());
		arenaPlayer.setTemplateFighter(defender);
		arenaPlayerService.update(arenaPlayer);
		
		ArenaCompetitor target = null;
		for(ArenaCompetitor competitor : arenaPlayer.getCompetitors()) {
			if(competitor.getId() == defender.getId() && competitor.getServerZone() == defender.getServerId()) {
				target = competitor;
				break;
			}
		}
		
		Formation formation = formationService.getEntity(playerContext.getId(), BattleType.ARENA);
		if(formation.getPositions().isEmpty()) {
			formation = formationService.getEntity(playerContext.getId(), BattleType.MAINLINE);
		}
		TemplateFighter attacker = formationService.buildFighter(formation);
		
		ArenaChallengeResponse.Builder response = ArenaChallengeResponse.newBuilder();
		response.setAttacker(build(attacker, arenaPlayer.getScore()));
		response.setDefender(build(defender, target == null ? 0 : target.getScore()));
		return response.build();
	}
	
	
	@PlayerCmd
	public void refresh(PlayerContext playerContext, ArenaRefreshRequest request) {
//TODO		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerContext.getId());
//		Asserts.isTrue(arenaPlayer.getHandRefreshTime() < DateTimeUtil.currMillis(), TipsCode.ARENA_REFRESH_CD);
		
		ArenaRefreshRpcRequest.Builder rpcRequest = ArenaRefreshRpcRequest.newBuilder();
		rpcRequest.setServerId(playerContext.getServerId());
		CrossContextManager.write(playerContext, ArenaRefreshRpcRequest.Proto.ID, rpcRequest.build());
	}
	
	@PlayerCrossCmd
	public ArenaRefreshResponse refresh(long playerId, ArenaRefreshRpcResponse rpcResponse) {
		ArenaPlayer arenaPlayer = arenaPlayerService.getEntity(playerId);
		arenaPlayer.getCompetitors().clear();
		for(PbArenaRanker pbArenaRanker : rpcResponse.getCompetitorsList()) {
			ArenaCompetitor competitor = new ArenaCompetitor();
			competitor.setRank(pbArenaRanker.getRank());
			competitor.setId(pbArenaRanker.getPlayerId());
			competitor.setServerZone(pbArenaRanker.getServerId());
			competitor.setNick(pbArenaRanker.getNick());
			competitor.setScore(pbArenaRanker.getScore());
			competitor.setFighting(pbArenaRanker.getFighting());
			competitor.setHead(pbArenaRanker.getHead());
			competitor.setImage(pbArenaRanker.getImage());
			arenaPlayer.getCompetitors().add(competitor);
		}
		arenaPlayer.setHandRefreshTime(DateTimeUtil.currMillis() + DateTimeUtil.ONE_MINUTE_MILLIS * 10);
		arenaPlayerService.update(arenaPlayer);
		
		ArenaRefreshResponse.Builder response = ArenaRefreshResponse.newBuilder();
		response.addAllCompetitors(rpcResponse.getCompetitorsList());
		return response.build();
	}
	
	
	@PlayerCmd
	public ArenaRecordResponse record(PlayerContext playerContext, ArenaRecordRequest request) {
		List<ArenaRecord> records = arenaRecordService.getEntities(playerContext.getId());
		
		ArenaRecordResponse.Builder response = ArenaRecordResponse.newBuilder();
		for(ArenaRecord record : records) {
			PbArenaRecord.Builder pbRecord = PbArenaRecord.newBuilder();
			pbRecord.setPlayerId(record.getOtherPid());
			pbRecord.setNick(record.getOtherNick());
			pbRecord.setHead(record.getHead());
			pbRecord.setBattleTime(record.getBattleTime());
			pbRecord.setResult(record.getResult());
			pbRecord.setScore(record.getScore());
			response.addRecords(pbRecord.build());
		}
		return response.build();
	}
	
	
	@CrossCmd
	public void dailySettle(CrossContext crossContext, ArenaDailySettleRpcMessage message) {
		logger.info("收到竞技场每日结算奖励消息，发奖人数{}", message.getRankersCount());
		List<Mail> mails = new ArrayList<>();
		for(PbArenaSettleRanker ranker : message.getRankersList()) {
			ArenaDailyRewardCfg rewardCfg = arenaDailyRewardCache.getReward(ranker.getRank());
			//TODO 邮件的参数
			Mail mail = mailService.newMail(ranker.getPlayerId(), MailTemplate.ARENA_DAILY, rewardCfg.getRewards(), GameCause.ARENA_DAILY_REWARD);
			mails.add(mail);
		}
		mailService.sendMail(mails, NoticeType.NORMAL);
	}
	
	@CrossCmd
	public void seasonSettle(CrossContext crossContext, ArenaSeasonSettleRpcMessage message) {
		logger.info("收到竞技场赛季结算奖励消息，发奖人数{}", message.getRankersCount());
		List<Mail> mails = new ArrayList<>();
		for(PbArenaSettleRanker ranker : message.getRankersList()) {
			ArenaSeasonRewardCfg rewardCfg = arenaSeasonRewardCache.getReward(ranker.getRank());
			//TODO 邮件的参数
			Mail mail = mailService.newMail(ranker.getPlayerId(), MailTemplate.ARENA_SEASON, rewardCfg.getRewards(), GameCause.ARENA_SEASON_REWARD);
			mails.add(mail);
		}
		mailService.sendMail(mails, NoticeType.NORMAL);
	}
}
