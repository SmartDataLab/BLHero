/**
 *
 */
package com.xiugou.x1.game.server.module.rank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.tool.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.ByteString;
import com.xiugou.x1.design.module.RankRewardCache;
import com.xiugou.x1.design.module.autogen.RankRewardAbstractCache.RankRewardCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.model.RankReward;
import com.xiugou.x1.game.server.module.rank.model.Ranker;
import com.xiugou.x1.game.server.module.rank.struct.RankFirstReach;

import pb.xiugou.x1.protobuf.rank.Rank.PbPlayerNormalTop3;
import pb.xiugou.x1.protobuf.rank.Rank.PbRanker;
import pb.xiugou.x1.protobuf.rank.Rank.RankRewardPushMessage;

/**
 * @author YY
 */
public abstract class PlayerRankService extends AbstractRankService {
	
    protected final Logger logger = LoggerFactory.getLogger(PlayerRankService.class);

    @Autowired
    protected PlayerService playerService;
    @Autowired
    protected RankRewardCache rankRewardCache;
    @Autowired
    protected RankRewardService rankRewardService;
    @Autowired
    protected FormationService formationService;
    @Autowired
    protected PlayerContextManager playerContextManager;

    @Override
    public String getEntityName(long entityId) {
        return playerService.getEntity(entityId).getNick();
    }

    public static Map<RankType, PlayerRankService> rankServices = new HashMap<>();

    public static PlayerRankService getService(RankType rankType) {
        return rankServices.get(rankType);
    }

    public PlayerRankService() {
    	rankServices.put(this.rankType(), this);
    }

    @Override
    public List<PbRanker> buildList(int page, int pageSize) {
        int startIndex = (page - 1) * pageSize + 1;
        int endIndex = page * pageSize;

        List<Ranker> rankers = this.getTopN(startIndex, endIndex);

        List<PbRanker> result = new ArrayList<>();
        for (int i = 0; i < rankers.size(); i++) {
            Ranker ranker = rankers.get(i);

            Player player = playerService.getEntity(ranker.getEntityId());
            PbRanker.Builder builder = PbRanker.newBuilder();
            builder.setScore(ranker.getScore());
            builder.setSubScore(ranker.getSubScore());
            builder.setRank(i + startIndex);

            builder.setEntityId(player.getId());
            builder.setName(player.getNick());
            builder.setHead(player.getHead());
            //构建前3的数据
            if(1 <= builder.getRank() && builder.getRank() <= 3) {
            	ByteString customInfo = buildTop3CustomInfo(player.getId());
            	builder.setData(customInfo);
            }
            result.add(builder.build());
        }
        return result;
    }

    public PbRanker buildSelf(long entityId) {
        Player player = playerService.getEntity(entityId);
        Ranker ranker = this.getRanker(entityId);
        if (ranker == null && initWhenMyRankNotFound(entityId)) {
			ranker = this.getRanker(entityId);
		}
        
        PbRanker.Builder builder = PbRanker.newBuilder();
        if (ranker == null) {
            builder.setScore(0);
            builder.setSubScore(0);
            builder.setRank(0);
            //TODO
        } else {
            builder.setScore(ranker.getScore());
            builder.setSubScore(ranker.getSubScore());
            builder.setRank(this.getRank(ranker));
        }
        builder.setEntityId(player.getId());
        builder.setName(player.getNick());
        builder.setHead(player.getHead());
        return builder.build();
    }
    
    /**
     * 在我的排名未找到时的初始化
     * @param playerId
     * @return 返回true表示初始化成功
     */
    protected abstract boolean initWhenMyRankNotFound(long playerId);

    protected void updateRankReward(long playerId, long score) {
        RankReward rankReward = rankRewardService.getEntity(this.rankType().getValue());
        if (rankReward.getMaxScore() >= score) {
            return;
        }
        boolean needPush = false;
        List<RankFirstReach> newFirstReaches = null;
        //更新最高分和最高奖励ID
        synchronized (rankReward) {
        	//找出当前最高分对应的奖励ID
            List<RankRewardCfg> rankRewardCfgs = rankRewardCache.findInTypeCollector(this.rankType().getValue());
            if(rankRewardCfgs == null) {
            	logger.error("未找到排行榜类型{}-{}的达成奖励配置", rankType().getValue(), rankType().getDesc());
            	return;
            } 
            int maxRewardId = 0;
            for (RankRewardCfg rankRewardCfg : rankRewardCfgs) {
            	if(rankRewardCfg.getRewardId() <= rankReward.getRewardId()) {
                	continue;
                }
            	if (rankRewardCfg.getCondition() > score) {
            		continue;
                }
            	if(rankRewardCfg.getRewardId() > maxRewardId) {
            		maxRewardId = rankRewardCfg.getRewardId();
            	}
            	RankFirstReach rankFirstReach = rankReward.getFirstReachs().get(rankRewardCfg.getRewardId());
            	if(rankFirstReach == null) {
            		Player player = playerService.getEntity(playerId);
            		rankFirstReach = new RankFirstReach();
            		rankFirstReach.setId(rankRewardCfg.getRewardId());
            		rankFirstReach.setPlayerId(player.getId());
            		rankFirstReach.setNick(player.getNick());
            		rankFirstReach.setHead(player.getHead());
            		rankFirstReach.setTime(DateTimeUtil.currMillis());
            		rankReward.getFirstReachs().put(rankFirstReach.getId(), rankFirstReach);
            		if(newFirstReaches == null) {
            			newFirstReaches = new ArrayList<>();
            		}
            		newFirstReaches.add(rankFirstReach);
            	}
            }
            if(maxRewardId > rankReward.getRewardId()) {
            	rankReward.setRewardId(maxRewardId);
            }
            rankReward.setMaxScore(score);
            rankRewardService.update(rankReward);
            needPush = true;
        }
        if(needPush) {
        	RankRewardPushMessage.Builder message = RankRewardPushMessage.newBuilder();
        	message.setRankType(rankType().getValue());
        	message.setAchieveId(rankReward.getRewardId());
        	if(newFirstReaches != null) {
        		for(RankFirstReach rankFirstReach : newFirstReaches) {
        			message.addFirstReaches(rankFirstReach.build());
        		}
        	}
        	playerContextManager.pushAll(RankRewardPushMessage.Proto.ID, message.build());
        }
    }

    /**
     * 构建定制数据
     * 可被重写
     * @param playerId
     * @return
     */
    protected ByteString buildTop3CustomInfo(long playerId) {
    	Formation formation = formationService.getEntity(playerId, BattleType.MAINLINE);
    	
    	PbPlayerNormalTop3.Builder builder = PbPlayerNormalTop3.newBuilder();
    	builder.setLeaderHero(formation.getMainHeroIdentity());
    	return builder.build().toByteString();
    }
}
