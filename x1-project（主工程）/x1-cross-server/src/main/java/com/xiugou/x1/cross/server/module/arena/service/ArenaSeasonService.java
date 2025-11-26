/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.game.GameContextManager;
import com.xiugou.x1.cross.server.foundation.service.SystemOneToManyService;
import com.xiugou.x1.cross.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.cross.server.module.arena.model.ArenaRanker;
import com.xiugou.x1.cross.server.module.arena.model.ArenaSeason;
import com.xiugou.x1.cross.server.module.arena.struct.ArenaTop;
import com.xiugou.x1.cross.server.module.server.model.CrossGroup;
import com.xiugou.x1.cross.server.module.server.service.CrossGroupSystemService;
import com.xiugou.x1.design.module.ArenaRobotCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.NameRandomCache;
import com.xiugou.x1.design.module.autogen.ArenaRobotAbstractCache.ArenaRobotCfg;
import com.xiugou.x1.design.module.autogen.MonsterAbstractCache.MonsterCfg;

import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaDailySettleRpcMessage;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.ArenaSeasonSettleRpcMessage;
import pb.xiugou.x1.protobuf.arena.ArenaRpc.PbArenaSettleRanker;

/**
 * @author hyy
 *
 */
@Service
public class ArenaSeasonService extends SystemOneToManyService<ArenaSeason> implements Lifecycle {

	@Autowired
	private ArenaRankerService arenaRankerService;
	@Autowired
	private CrossGroupSystemService crossGroupSystemService;
	@Autowired
	private ArenaRobotCache arenaRobotCache;
	@Autowired
	private NameRandomCache nameRandomCache;
	@Autowired
	private MonsterCache monsterCache;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	//<跨服分组ID，赛区上下文>
	private ConcurrentMap<Integer, ArenaContext> contextMap = new ConcurrentHashMap<>();
	
	@Override
	public List<ArenaSeason> getEntities() {
		return this.repository().getAllInCache();
	}

	@Override
	public void start() throws Exception {
		List<ArenaSeason> seasons = getEntities();
		for(ArenaSeason season : seasons) {
			ArenaContext arenaContext = new ArenaContext(season, arenaRankerService);
			List<ArenaRanker> rankers = arenaRankerService.getRankers(season.getId(), season.getSeason());
			arenaContext.addRankers(rankers);
			contextMap.put(arenaContext.getCrossGroup(), arenaContext);
			logger.info("加载竞技场赛区{}，人数{}", arenaContext.getCrossGroup(), rankers.size());
		}
	}
	
	/**
	 * 获取玩家所在的竞技场赛区上下文
	 * @param gameServerId
	 * @return
	 */
	public ArenaContext getContext(int gameServerId) {
		CrossGroup crossGroup = crossGroupSystemService.getCrossGroup(gameServerId);
		ArenaContext arenaContext = contextMap.get(crossGroup.getId());
		if(arenaContext == null) {
			synchronized (this) {
				arenaContext = contextMap.get(crossGroup.getId());
				if(arenaContext == null) {
					ArenaSeason season = new ArenaSeason();
					season.setId(crossGroup.getId());
					season.setSeason(1);
					this.insert(season);
					arenaContext = new ArenaContext(season, arenaRankerService);
					arenaContext.addRankers(buildRobots(season));
					contextMap.put(arenaContext.getCrossGroup(), arenaContext);
				}
			}
		}
		return arenaContext;
	}
	
	private List<ArenaRanker> buildRobots(ArenaSeason season) {
		//生成机器人
		int robotId = 0;
		List<ArenaRanker> robots = new ArrayList<>();
		List<ArenaRobotCfg> robotCfgs = arenaRobotCache.findInSeasonCollector(season.getSeason());
		if(robotCfgs == null) {
			ArenaRobotCfg robotCfg = arenaRobotCache.all().get(arenaRobotCache.all().size() - 1);
			robotCfgs = arenaRobotCache.findInSeasonCollector(robotCfg.getSeason());
		}
		for(ArenaRobotCfg arenaRobotCfg : robotCfgs) {
			List<Integer> monsterIds = new ArrayList<>(arenaRobotCfg.getMonsterIds());
			for(int i = arenaRobotCfg.getRankUp(); i <= arenaRobotCfg.getRankDown(); i++) {
				robotId += 1;
				ArenaRanker robot = new ArenaRanker();
				robot.setRobotCfg(arenaRobotCfg.getId());
				robot.setPlayerId(robotId);
				robot.setCrossGroup(season.getId());
				robot.setSeason(season.getSeason());
				int score = RandomUtil.closeClose(arenaRobotCfg.getScoreMin(), arenaRobotCfg.getScoreMax());
				robot.setScore(score);
				robot.setRobot(true);
				robot.setServerId(0);
				String nick = nameRandomCache.generationRandomName(applicationSettings.getCrossLanguage(), Math.random() < 0.5 ? 1 : 2);
				robot.setRobotNick(nick);
				Collections.shuffle(monsterIds);
				for(int j = 0; j < arenaRobotCfg.getMonsterNum() && j < monsterIds.size(); j++) {
					int monsterId = monsterIds.get(j);
					robot.getMonsterIds().add(monsterId);
				}
				if(robot.getMonsterIds().isEmpty()) {
					//单纯为了容错
					robot.getMonsterIds().add(7002001);
				}
				int firstMonsterId = robot.getMonsterIds().get(0);
				MonsterCfg monsterCfg = monsterCache.getOrThrow(firstMonsterId);
				robot.setRobotImage(monsterCfg.getIdentity());
				robot.setRobotHead(monsterCfg.getIdentity());
				robot.setRobotFighting(monsterCfg.getFighting());
				robots.add(robot);
			}
		}
		arenaRankerService.insertAll(robots);
		return robots;
	}
	
	protected void settleDaily() {
		for(ArenaContext arenaContext : contextMap.values()) {
			List<ArenaRanker> rankers = arenaContext.getAllTopN();
			for(int i = 0; i < rankers.size(); i++) {
				ArenaRanker ranker = rankers.get(i);
				ranker.setDailyRank(i + 1);
			}
			arenaRankerService.updateAll(rankers);
			
			Map<Integer, List<ArenaRanker>> serverRankerMap = ListMapUtil.fillListMap(rankers, ArenaRanker::getServerId);
			for(Entry<Integer, List<ArenaRanker>> entry : serverRankerMap.entrySet()) {
				int serverId = entry.getKey();
				if(serverId <= 0) {
					//机器人的服务器ID为0
					continue;
				}
				ArenaDailySettleRpcMessage.Builder message = ArenaDailySettleRpcMessage.newBuilder();
				for(ArenaRanker ranker : entry.getValue()) {
					PbArenaSettleRanker.Builder pbRanker = PbArenaSettleRanker.newBuilder();
					pbRanker.setPlayerId(ranker.getPlayerId());
					pbRanker.setRank(ranker.getDailyRank());
					message.addRankers(pbRanker).build();
				}
				GameContextManager.writeTo(serverId, ArenaDailySettleRpcMessage.Proto.ID, message.build());
				logger.info("竞技场发送服务器{}的每日结算奖励，奖励人数{}", serverId, entry.getValue().size());
			}
		}
	}
	
	protected void settleSeason() {
		for(ArenaContext arenaContext : contextMap.values()) {
			List<ArenaRanker> rankers = arenaContext.getAllTopN();
			ArenaSeason arenaSeason = arenaContext.getSeason();
			arenaSeason.getTops().clear();
			
			for(int i = 0; i < rankers.size(); i++) {
				ArenaRanker ranker = rankers.get(i);
				ranker.setSeasonRank(i + 1);
				if(i < 3) {
					ArenaTop arenaTop = new ArenaTop();
					arenaTop.setPlayerId(ranker.getPlayerId());
					arenaTop.setServerId(ranker.getServerId());
					arenaTop.setRank(i + 1);
					arenaTop.setRobot(ranker.isRobot());
					arenaTop.setNick(ranker.getRobotNick());
					arenaTop.setImage(ranker.getRobotImage());
					arenaTop.setFighting(ranker.getRobotFighting());
					arenaSeason.getTops().add(arenaTop);
				}
			}
			arenaRankerService.updateAll(rankers);
			this.update(arenaSeason);
			
			Map<Integer, List<ArenaRanker>> serverRankerMap = ListMapUtil.fillListMap(rankers, ArenaRanker::getServerId);
			for(Entry<Integer, List<ArenaRanker>> entry : serverRankerMap.entrySet()) {
				int serverId = entry.getKey();
				if(serverId <= 0) {
					//机器人的服务器ID为0
					continue;
				}
				
				ArenaSeasonSettleRpcMessage.Builder message = ArenaSeasonSettleRpcMessage.newBuilder();
				for(ArenaRanker ranker : entry.getValue()) {
					PbArenaSettleRanker.Builder pbRanker = PbArenaSettleRanker.newBuilder();
					pbRanker.setPlayerId(ranker.getPlayerId());
					pbRanker.setRank(ranker.getSeasonRank());
					message.addRankers(pbRanker).build();
				}
				GameContextManager.writeTo(serverId, ArenaSeasonSettleRpcMessage.Proto.ID, message.build());
				logger.info("竞技场发送服务器{}的赛季结算奖励，奖励人数{}", serverId, entry.getValue().size());
			}
		}
		//开启新赛季
		List<ArenaSeason> seasons = getEntities();
		for(ArenaSeason season : seasons) {
			season.setSeason(season.getSeason() + 1);
			ArenaContext arenaContext = new ArenaContext(season, arenaRankerService);
			arenaContext.addRankers(buildRobots(season));
			contextMap.put(arenaContext.getCrossGroup(), arenaContext);
		}
		this.updateAll(seasons);
	}
}
