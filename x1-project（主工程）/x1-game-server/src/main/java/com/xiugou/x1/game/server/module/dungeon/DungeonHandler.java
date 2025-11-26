package com.xiugou.x1.game.server.module.dungeon;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.dungeon.model.Dungeon;
import com.xiugou.x1.game.server.module.dungeon.service.DungeonService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.dungeon.Dungeon.DungeonInfoResponse;

/**
 * @author yh
 * @date 2023/10/11
 * @apiNote
 */
@Controller
public class DungeonHandler extends AbstractModuleHandler {
	
    @Autowired
    private DungeonService dungeonService;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        Dungeon dungeon = dungeonService.getEntity(playerId);
        
        Set<Integer> normalDungeons = dungeon.getNormalDungeons();
        DungeonInfoResponse.Builder response = DungeonInfoResponse.newBuilder();
        response.addAllNormalDungeons(normalDungeons);
        playerContextManager.push(playerId, DungeonInfoResponse.Proto.ID, response.build());
    }
}
