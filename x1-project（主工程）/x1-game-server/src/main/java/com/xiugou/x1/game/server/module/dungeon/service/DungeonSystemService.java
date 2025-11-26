package com.xiugou.x1.game.server.module.dungeon.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.mail.MailArgs;
import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.module.dungeon.struct.DungeonSystemData;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.rank.model.Ranker;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;

/**
 * @author yh
 * @date 2023/8/18
 * @apiNote 简单副本统一结算类
 */
public abstract class DungeonSystemService<T extends AbstractEntity> extends OneToOneService<T> {
    @Autowired
    private MailService mailService;
    @Autowired
    private BattleTypeCache battleTypeCache;

    public abstract List<RewardThing> getRankReward(int ranking);

    protected void settle(AbsDungeonSystemEntity entity, DungeonSystemData dungeonSystemData) {

        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(dungeonSystemData.getBattleType());

        logger.info("{}排行榜结算开始，当前赛季{}，结算时间{}", battleTypeCfg.getName(), entity.getRound(), entity.getNextResetTime());

        PlayerRankService playerRankService = PlayerRankService.getService(dungeonSystemData.getRankType());

        List<Ranker> rankers = playerRankService.getAll();
        logger.info("{}排行榜结算，排名人数{}", battleTypeCfg.getName(), rankers.size());
        List<Mail> rankMails = new ArrayList<>();

        for (int i = 0; i < rankers.size(); i++) {
            Ranker ranker = rankers.get(i);
            int rank = i + 1;
            //根据排名去拿对应的赛季结算奖励
            List<RewardThing> rewards = getRankReward(rank);
            //发放排名奖励
            Mail mail = mailService.newMail(ranker.getEntityId(), dungeonSystemData.getMailTemplate()
                    , null, MailArgs.build(ranker.getEntityId(), rank), rewards, dungeonSystemData.getGameCause());
            rankMails.add(mail);

            if (rank <= 50) {
                logger.info("{}排行榜结算，第{}名玩家ID{}，积分{}", battleTypeCfg.getName(), rank, ranker.getEntityId(), ranker.getScore());
            }
        }
        mailService.sendMail(rankMails, NoticeType.NORMAL);

        //更新排行榜
        playerRankService.clearRank();
        for (Ranker ranker : rankers) {
            long score = Math.max(ranker.getScore() - dungeonSystemData.getBackLevel(), 0);
            if (score <= 0) {
                continue;
            }
            playerRankService.updateRank(ranker.getEntityId(), score, 0);
        }

        entity.setRound(entity.getRound() + 1);
        entity.setNextResetTime(dungeonSystemData.getNextResetTime());

        //todo 外部更新
        logger.info("{}排行榜结算完成，下一赛季{}，下次结算时间{}", battleTypeCfg.getName(), entity.getRound(), entity.getNextResetTime());
    }
}
