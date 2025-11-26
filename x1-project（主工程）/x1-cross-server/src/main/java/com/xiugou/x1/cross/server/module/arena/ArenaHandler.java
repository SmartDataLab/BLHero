/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.template.TemplateFighter;
import com.xiugou.x1.battle.template.TemplateSprite;
import com.xiugou.x1.cross.server.foundation.game.GameContext;
import com.xiugou.x1.cross.server.module.arena.model.ArenaRanker;
import com.xiugou.x1.cross.server.module.arena.model.ArenaSeason;
import com.xiugou.x1.cross.server.module.arena.model.ArenaSystem;
import com.xiugou.x1.cross.server.module.arena.service.ArenaContext;
import com.xiugou.x1.cross.server.module.arena.service.ArenaSeasonService;
import com.xiugou.x1.cross.server.module.arena.service.ArenaSystemService;
import com.xiugou.x1.cross.server.module.arena.struct.ArenaTop;
import com.xiugou.x1.cross.server.module.player.service.PlayerService;
import com.xiugou.x1.design.module.ArenaRobotCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.autogen.ArenaRobotAbstractCache.ArenaRobotCfg;

import pb.xiugou.x1.protobuf.arena.Arena.ArenaInfoResponse;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaTopInfoRequest;
import pb.xiugou.x1.protobuf.arena.Arena.ArenaTopInfoResponse;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaRanker;
import pb.xiugou.x1.protobuf.arena.Arena.PbArenaTopPlayer;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaBattleEndRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaBattleEndRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeRobotRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaChallengeRobotRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaInfoRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaRefreshRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaRefreshRpcResponse;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaResumeInfoRpcRequest;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaResumeInfoRpcResponse;
import pojo.xiugou.x1.pojo.module.player.model.PlayerSnapshot;

/**
 * @author hyy
 *
 */
@Controller
public class ArenaHandler {

	@Autowired
	private ArenaSeasonService arenaSeasonService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private ArenaSystemService arenaSystemService;
	@Autowired
	private MonsterCache monsterCache;
	@Autowired
	private ArenaRobotCache arenaRobotCache;
	
	
	@PlayerCrossCmd
	public ArenaTopInfoResponse topInfo(GameContext gameContext, long playerId, ArenaTopInfoRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		ArenaSeason arenaSeason = arenaContext.getSeason();
		
		ArenaTopInfoResponse.Builder response = ArenaTopInfoResponse.newBuilder();
		for(ArenaTop arenaTop : arenaSeason.getTops()) {
			PbArenaTopPlayer.Builder pbRanker = PbArenaTopPlayer.newBuilder();
			pbRanker.setRank(arenaTop.getRank());
			if(!arenaTop.isRobot()) {
				PlayerSnapshot player = playerService.getPlayer(arenaTop.getPlayerId(), arenaTop.getServerId());
				pbRanker.setPlayerId(player.getId());
				pbRanker.setNick(player.getNick());
				pbRanker.setImage(player.getImage());
				pbRanker.setFighting(player.getFighting());
			} else {
				pbRanker.setPlayerId(0);
				pbRanker.setNick(arenaTop.getNick());
				pbRanker.setImage(arenaTop.getImage());
				pbRanker.setFighting(arenaTop.getFighting());
			}
			response.addTopPlayers(pbRanker.build());
		}
		return response.build();
	}
	
	@PlayerCrossCmd
	public ArenaResumeInfoRpcResponse resumeInfo(GameContext gameContext, long playerId, ArenaResumeInfoRpcRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		ArenaRanker ranker = arenaContext.getRanker(playerId, request.getServerId());
		ArenaSystem arenaSystem = arenaSystemService.getEntity();
		
		ArenaResumeInfoRpcResponse.Builder response = ArenaResumeInfoRpcResponse.newBuilder();
		response.setRank(arenaContext.getRank(ranker));
		response.setScore(ranker.getScore());
		response.setSettleTime(arenaSystem.getSettleTime());
		return response.build();
	}
	
	@PlayerCrossCmd
	public ArenaInfoResponse info(GameContext gameContext, long playerId, ArenaInfoRpcRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		
		ArenaInfoResponse.Builder response = ArenaInfoResponse.newBuilder();
		List<ArenaRanker> rankers = arenaContext.getTopN(1, 5);
		//竞技场前5
		for(int i = 0; i < rankers.size(); i++) {
			ArenaRanker ranker = rankers.get(i);
			if(ranker.isRobot()) {
				PbArenaTopPlayer.Builder pbRanker = PbArenaTopPlayer.newBuilder();
				pbRanker.setRank(i + 1);
				pbRanker.setPlayerId(ranker.getPlayerId());
				pbRanker.setNick(ranker.getRobotNick());
				pbRanker.setImage(ranker.getRobotImage());
				response.addTopPlayers(pbRanker.build());
			} else {
				PlayerSnapshot player = playerService.getPlayer(ranker.getPlayerId(), ranker.getServerId());
				PbArenaTopPlayer.Builder pbRanker = PbArenaTopPlayer.newBuilder();
				pbRanker.setRank(i + 1);
				pbRanker.setPlayerId(player.getId());
				pbRanker.setNick(player.getNick());
				pbRanker.setImage(player.getImage());
				response.addTopPlayers(pbRanker.build());
			}
		}
		ArenaRanker ranker = arenaContext.getRanker(playerId, request.getServerId());
		response.setMyRank(build(ranker, arenaContext));
		if(request.getRefresh()) {
			List<ArenaRanker> competitors = refresh(ranker, arenaContext);
			for(ArenaRanker competitor : competitors) {
				response.addCompetitors(build(competitor, arenaContext));
			}
		}
		return response.build();
	}
	
	
	public List<ArenaRanker> refresh(ArenaRanker ranker, ArenaContext arenaContext) {
		int myRank = arenaContext.getRank(ranker);
		if(myRank == 0) {
			myRank = arenaContext.getMaxRank();
		}
		List<ArenaRanker> result = new ArrayList<>();
		
		List<ArenaRanker> competitorOne = arenaContext.getTopN(myRank - 5, myRank + 5);
		if(!competitorOne.isEmpty()) {
			Collections.shuffle(competitorOne);
			while(!competitorOne.isEmpty()) {
				ArenaRanker one = competitorOne.remove(0);
				if(one.getPlayerId() != ranker.getPlayerId()) {
					result.add(one);
					break;
				}
			}
		}
		//比自己高的排名，其排名的数字会更小
		List<ArenaRanker> competitorTwo = arenaContext.getTopN(myRank - 5, myRank - 15);
		if(!competitorTwo.isEmpty()) {
			Collections.shuffle(competitorTwo);
			ArenaRanker two = competitorTwo.remove(0);
			result.add(two);
		}
		//比自己低的排名，其排名的数字会更大
		List<ArenaRanker> competitorThree = arenaContext.getTopN(myRank + 5, myRank + 15);
		if(!competitorThree.isEmpty()) {
			Collections.shuffle(competitorThree);
			ArenaRanker three = competitorThree.remove(0);
			result.add(three);
		}
		if(result.size() < 3) {
			List<ArenaRanker> pools = new ArrayList<>();
			pools.addAll(competitorOne);
			pools.addAll(competitorTwo);
			pools.addAll(competitorThree);
			Collections.shuffle(pools);
			for(int i = 0; i < pools.size() && result.size() < 3; i++) {
				ArenaRanker temp = pools.get(i);
				result.add(temp);
			}
		}
		return result;
	}
	
	public PbArenaRanker build(ArenaRanker ranker, ArenaContext arenaContext) {
		int rank = arenaContext.getRank(ranker.getPlayerId(), ranker.getServerId());
		
		PbArenaRanker.Builder builder = PbArenaRanker.newBuilder();
		builder.setRank(rank);
		builder.setPlayerId(ranker.getPlayerId());
		if(ranker.isRobot()) {
			builder.setNick(ranker.getRobotNick());
			builder.setFighting(0);//TODO 机器人的战力
			builder.setHead(ranker.getRobotHead() + "");
			builder.setServerId(0);
			builder.setImage(ranker.getRobotImage());
		} else {
			PlayerSnapshot player = playerService.getPlayer(ranker.getPlayerId(), ranker.getServerId());
			builder.setNick(player.getNick());
			builder.setFighting(player.getFighting());
			builder.setHead(player.getHead());
			builder.setServerId(player.getServerId());
			builder.setImage(player.getImage());
		}
		builder.setScore(ranker.getScore());
		return builder.build();
	}
	
	@PlayerCrossCmd
	public ArenaRefreshRpcResponse refresh(GameContext gameContext, long playerId, ArenaRefreshRpcRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		ArenaRanker ranker = arenaContext.getRanker(playerId, request.getServerId());
		
		List<ArenaRanker> competitors = refresh(ranker, arenaContext);
		
		ArenaRefreshRpcResponse.Builder response = ArenaRefreshRpcResponse.newBuilder();
		for(ArenaRanker competitor : competitors) {
			response.addCompetitors(build(competitor, arenaContext));
		}
		return response.build();
	}
	
	@PlayerCrossCmd
	public ArenaBattleEndRpcResponse battleEnd(GameContext gameContext, long playerId, ArenaBattleEndRpcRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		ArenaRanker ranker = arenaContext.getRanker(playerId, request.getServerId());
		
		ArenaRanker otherRanker = arenaContext.getRanker(request.getOtherPlayerId(), request.getOtherServerId());
		
		int difScore = 0;
		if(request.getResult() == 1) {
			//胜利
			long deltaScore = otherRanker.getScore() - ranker.getScore();
			difScore = 20 + (int)(deltaScore / 10);
			if(difScore <= 10) {
				difScore = 10;
			}
			ranker.setHasFight(true);
			arenaContext.updateRank(playerId, ranker.getServerId(), ranker.getScore() + difScore, 0);
		} else {
			//失败
			long deltaScore = otherRanker.getScore() - ranker.getScore();
			difScore = -10;
			if(deltaScore > 0) {
				//对手的分数比我高则可以减少积分损失
				difScore += deltaScore / 10;
				if(difScore >= -3) {
					difScore = -3;
				}
			}
			ranker.setHasFight(true);
			arenaContext.updateRank(playerId, ranker.getServerId(), ranker.getScore() + difScore, 0);
		}
		
		ArenaBattleEndRpcResponse.Builder response = ArenaBattleEndRpcResponse.newBuilder();
		response.setPlayerId(request.getPlayerId());
		response.setServerId(request.getServerId());
		response.setScore(difScore);
		return response.build();
	}
	
	@PlayerCrossCmd
	public ArenaChallengeRobotRpcResponse challenge(GameContext gameContext, long playerId, ArenaChallengeRobotRpcRequest request) {
		ArenaContext arenaContext = arenaSeasonService.getContext(gameContext.getId());
		ArenaRanker ranker = arenaContext.getRanker(request.getPlayerId(), request.getServerId());
		
		ArenaRobotCfg arenaRobotCfg = arenaRobotCache.getOrThrow(ranker.getRobotCfg());
		
		TemplateFighter templateFighter = new TemplateFighter();
		templateFighter.setId(ranker.getPlayerId());
		templateFighter.setFighting(0);//TODO 计算机器人的战斗力
		templateFighter.setNick(ranker.getRobotNick());
		templateFighter.setLevel(arenaRobotCfg.getLevel());
		templateFighter.setServerId(0);
		templateFighter.setType(2);
		
		for(int i = 0; i < ranker.getMonsterIds().size(); i++) {
			int monsterId = ranker.getMonsterIds().get(i);
			MonsterConfig monsterCfg = monsterCache.getOrThrow(monsterId);
			
			TemplateSprite sprite = new TemplateSprite();
			sprite.setIdentity(monsterCfg.getConfigId());
			sprite.setLevel(monsterCfg.getLevel());
			sprite.setPos(i + 1);
			sprite.setPanelAttr((BattleAttr)monsterCfg.getPanelAttr());
			templateFighter.getSprites().add(sprite);
		}
		
		ArenaChallengeRobotRpcResponse.Builder response = ArenaChallengeRobotRpcResponse.newBuilder();
		response.setPlayerId(request.getPlayerId());
		response.setServerId(request.getServerId());
		response.setFighterJson(GsonUtil.toJson(templateFighter));
		return response.build();
	}
}
