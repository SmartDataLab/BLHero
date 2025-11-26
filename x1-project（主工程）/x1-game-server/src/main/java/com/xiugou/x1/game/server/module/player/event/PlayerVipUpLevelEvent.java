package com.xiugou.x1.game.server.module.player.event;

import com.xiugou.x1.game.server.module.player.model.Player;

/**
 * @author yh
 * @date 2023/9/25
 * @apiNote
 */
public class PlayerVipUpLevelEvent {
    private Player player;

    public static PlayerVipUpLevelEvent of(Player player) {
        PlayerVipUpLevelEvent event = new PlayerVipUpLevelEvent();
        event.player = player;
        return event;
    }

    public Player getPlayer() {
        return player;
    }
}
