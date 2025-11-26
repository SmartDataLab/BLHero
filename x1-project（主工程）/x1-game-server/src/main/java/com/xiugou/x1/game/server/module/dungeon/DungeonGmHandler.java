package com.xiugou.x1.game.server.module.dungeon;

import java.util.List;
import java.util.Set;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.module.DungeonCache;
import com.xiugou.x1.design.module.autogen.DungeonAbstractCache.DungeonCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.mainline.MainlineHandler;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.service.MainlineSceneService;

/**
 * @author yh
 * @date 2023/9/18
 * @apiNote
 */
@Controller
public class DungeonGmHandler {
    @Autowired
    private MainlineSceneService mainlineSceneService;
    @Autowired
    private DungeonCache dungeonCache;
    @Autowired
    private MainlineHandler mainlineHandler;

    @PlayerGmCmd(command = "COMPLETE_ALL_DUNGEON")
    public void CompleteAllDungeon(PlayerContext playerContext, String[] params) {

        List<DungeonCfg> all = dungeonCache.all();
        int mainLineId = 0;
        MainlineScene mainlineScene = null;
        for (int i = 0; i < all.size(); i++) {
            DungeonCfg dungeonCfg = all.get(i);
            if (dungeonCfg.getMainlineId() == 0) {
                continue;
            }
            if (mainLineId != dungeonCfg.getMainlineId()) {
                mainLineId = dungeonCfg.getMainlineId();
                if (mainlineScene != null) {
                    mainlineSceneService.update(mainlineScene);
                }
                mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), mainLineId);
            }
            Set<Integer> dungeons = mainlineScene.getDungeons();
            if (dungeons.contains(dungeonCfg.getId())) {
                continue;
            }
            dungeons.add(dungeonCfg.getId());
        }
        mainlineHandler.pushInfo(playerContext);

    }
}
