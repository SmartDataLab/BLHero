package com.xiugou.x1.game.server.module.dungeon.struct;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.rank.constant.RankType;

import java.time.LocalDateTime;

/**
 * @author yh
 * @date 2023/8/18
 * @apiNote 简单副本结算结构体
 */
public class DungeonSystemData {
    // 排行榜类型
    private RankType rankType;
    // 邮件模板
    private MailTemplate mailTemplate;
    // 回退关卡
    private int backLevel;
    private int battleType;

    private GameCause gameCause;

    private LocalDateTime nextResetTime =LocalDateTime.now();


    public RankType getRankType() {
        return rankType;
    }

    public void setRankType(RankType rankType) {
        this.rankType = rankType;
    }

    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public int getBackLevel() {
        return backLevel;
    }

    public void setBackLevel(int backLevel) {
        this.backLevel = backLevel;
    }

    public int getBattleType() {
        return battleType;
    }

    public void setBattleType(int battleType) {
        this.battleType = battleType;
    }

    public GameCause getGameCause() {
        return gameCause;
    }

    public void setGameCause(GameCause gameCause) {
        this.gameCause = gameCause;
    }

    public LocalDateTime getNextResetTime() {
        return nextResetTime;
    }

    public void setNextResetTime(LocalDateTime nextResetTime) {
        this.nextResetTime = nextResetTime;
    }
}
