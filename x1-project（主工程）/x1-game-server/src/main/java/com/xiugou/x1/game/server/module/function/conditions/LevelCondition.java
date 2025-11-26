package com.xiugou.x1.game.server.module.function.conditions;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yh
 * @date 2023/6/14
 * @apiNote
 */
@Component
public class LevelCondition extends FunctionCondition {
    @Autowired
    PlayerService playerService;

    @Override
    protected OpenfunctionType getOpenfunctionType() {
        return OpenfunctionType.PLAYER_LEVEL;
    }

    @Override
    public boolean functionOpenOrNot(long pid, int condition) {
        Player player = playerService.getEntity(pid);
        return player.getLevel() >= condition;
    }
}
